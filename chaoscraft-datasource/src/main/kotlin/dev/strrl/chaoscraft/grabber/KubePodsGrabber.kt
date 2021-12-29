package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.Workload
import io.fabric8.kubernetes.client.KubernetesClient

class KubePodsGrabber(private val kubeClient: KubernetesClient) : WorkloadGrabber {
    override fun listWorkloads(): List<Workload> {
        val pods = this.kubeClient.pods().inAnyNamespace().list()
        return pods.items.map { Workload(it.metadata.namespace, it.metadata.name) }
    }

    override fun listWorkloadsInNamespace(namespace: String): List<Workload> {
        val pods = this.kubeClient.pods().inNamespace(namespace).list()
        return pods.items.map { Workload(it.metadata.namespace, it.metadata.name) }
    }
}