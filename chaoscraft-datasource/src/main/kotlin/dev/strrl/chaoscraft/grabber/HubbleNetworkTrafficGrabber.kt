package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.NetworkTraffic
import dev.strrl.chaoscraft.api.Workload
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import observer.ObserverGrpc
import observer.ObserverOuterClass
import observer.ObserverOuterClass.GetFlowsResponse.ResponseTypesCase.FLOW
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

class HubbleNetworkTrafficGrabber(
    ciliumGrpcHost: String = "localhost",
    ciliumGrpcPort: Int = 4245,
) : NetworkTrafficGrabber {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private val aggregator = FlowAggregator()

    init {
        val channel = ManagedChannelBuilder.forAddress(ciliumGrpcHost, ciliumGrpcPort).usePlaintext().build()!!
        val stub = ObserverGrpc.newStub(channel)
        val request = ObserverOuterClass.GetFlowsRequest.newBuilder().setFollow(true).build()
        stub.getFlows(request, object : StreamObserver<ObserverOuterClass.GetFlowsResponse> {
            override fun onNext(response: ObserverOuterClass.GetFlowsResponse) {
                if (response.responseTypesCase == FLOW) {
                    val from = response.flow.source
                    val to = response.flow.destination
                    if (from.namespace.isEmpty() || from.podName.isEmpty() || to.namespace.isEmpty() || to.podName.isEmpty()) {
                        return
                    }
                    aggregator.feed(
                        Traffic(
                            Endpoint(from.namespace, from.podName),
                            Endpoint(to.namespace, to.podName),
                        )
                    )
                }
            }

            override fun onError(t: Throwable?) {
                logger.error("onError: $t")
            }

            override fun onCompleted() {
                logger.info("cilium flow completed")
            }
        })
    }

    override fun listTraffics(): List<NetworkTraffic> {
        val result = this.aggregator.listOrdered().map {
            NetworkTraffic(
                Workload(it.first.from.namespace, it.first.from.name),
                Workload(it.first.to.namespace, it.first.to.name),
                it.second
            )
        }
        this.aggregator.clear()
        return result
    }
}

class FlowAggregator {
    private val store: MutableMap<Traffic, AtomicInteger> = ConcurrentHashMap()
    private val queue: BlockingQueue<Traffic> = LinkedBlockingQueue()

    init {
        Thread {
            while (true) {
                val traffic = queue.take()
                store.computeIfAbsent(traffic) { AtomicInteger(0) }.incrementAndGet()
            }
        }.start()
    }

    fun feed(traffic: Traffic) {
        queue.put(traffic)
    }

    fun listOrdered(): List<Pair<Traffic, Int>> {
        return HashMap(store).entries.parallelStream().map {
            Pair(it.key, it.value.get())
        }.sorted(Comparator.comparingInt { it.second }).collect(Collectors.toList())
    }

    fun clear() {
        this.store.clear()
    }
}

data class Endpoint(
    val namespace: String,
    val name: String,
)

data class Traffic(
    val from: Endpoint,
    val to: Endpoint,
)