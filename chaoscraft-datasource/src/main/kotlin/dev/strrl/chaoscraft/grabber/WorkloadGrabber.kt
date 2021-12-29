package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.Workload

interface WorkloadGrabber {
    fun listWorkloads(): List<Workload>
    fun listWorkloadsInNamespace(namespace: String): List<Workload>
}