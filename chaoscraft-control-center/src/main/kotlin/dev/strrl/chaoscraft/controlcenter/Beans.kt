package dev.strrl.chaoscraft.controlcenter

import dev.strrl.chaoscraft.grabber.KubePodsGrabber
import dev.strrl.chaoscraft.grabber.KubeTopResourceUsageGrabber
import dev.strrl.chaoscraft.grabber.WorkloadGrabber
import dev.strrl.chaoscraft.grabber.WorkloadResourceUsageGrabber
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import io.fabric8.kubernetes.client.KubernetesClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Beans {
    @Bean
    fun kubeClient(): KubernetesClient {
        return DefaultKubernetesClient()
    }

    @Bean
    fun kubePodsGrabber(kubeClient: KubernetesClient): WorkloadGrabber {
        return KubePodsGrabber(kubeClient)
    }

    @Bean
    fun kubePodsTopGrabber(kubeClient: KubernetesClient): WorkloadResourceUsageGrabber {
        return KubeTopResourceUsageGrabber(kubeClient)
    }
}
