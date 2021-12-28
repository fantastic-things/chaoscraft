package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.ResourceUsage
import dev.strrl.chaoscraft.api.Workload

interface ResourceUsageGrabber {
    fun allResourceUsage(): List<Pair<Workload, List<ResourceUsage>>>
    fun resourceUsageAbout(workload: Workload): List<ResourceUsage>
}