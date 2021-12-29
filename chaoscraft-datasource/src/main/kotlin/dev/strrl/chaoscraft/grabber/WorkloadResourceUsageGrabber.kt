package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.ResourceUsage
import dev.strrl.chaoscraft.api.Workload

interface WorkloadResourceUsageGrabber {
    fun listWorkloadResourceUsages(): List<Pair<Workload, List<ResourceUsage>>>
    fun listResourceUsageAbout(workload: Workload): List<ResourceUsage>
}