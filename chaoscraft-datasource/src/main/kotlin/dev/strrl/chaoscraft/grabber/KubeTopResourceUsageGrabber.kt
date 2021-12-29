package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.ResourceType
import dev.strrl.chaoscraft.api.ResourceUsage
import dev.strrl.chaoscraft.api.Workload
import io.fabric8.kubernetes.api.model.Quantity
import io.fabric8.kubernetes.api.model.metrics.v1beta1.ContainerMetrics
import io.fabric8.kubernetes.client.KubernetesClient
import org.slf4j.LoggerFactory

class KubeTopResourceUsageGrabber(private val kubeClient: KubernetesClient) : WorkloadResourceUsageGrabber {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val kubePodsGrabber = KubePodsGrabber(kubeClient)

    override fun listWorkloadResourceUsages(): List<Pair<Workload, List<ResourceUsage>>> {
        return kubePodsGrabber.listWorkloads().map {
            Pair(it, listResourceUsageAbout(it))
        }
    }

    override fun listResourceUsageAbout(workload: Workload): List<ResourceUsage> {
        return aggregateContainerMetrics(
            kubeClient.top().pods().metrics(workload.namespace, workload.name).containers
        )
    }

    private fun aggregateContainerMetrics(
        containerMetrics: List<ContainerMetrics>
    ): List<ResourceUsage> {
        var cpu = 0.0
        var memInBytes = 0.0

        for (metric in containerMetrics) {
            cpu += Quantity.getAmountInBytes(metric.usage["cpu"]!!).toDouble()
            memInBytes += Quantity.getAmountInBytes(metric.usage["memory"]!!).toDouble()
        }
        return listOf(
            ResourceUsage(cpu, 0.0, ResourceType.CPU),
            ResourceUsage(memInBytes, 0.0, ResourceType.Memory),
        )
    }
}
