package dev.strrl.chaoscraft.controlcenter.web

import dev.strrl.chaoscraft.api.ResourceUsage
import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.grabber.WorkloadResourceUsageGrabber
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/top")
class Top(private val grabber: WorkloadResourceUsageGrabber) {
    @GetMapping("/kube/pods/all")
    fun workloadTop(): List<WorkloadResourceUsage> {
        return grabber.listWorkloadResourceUsages().map { WorkloadResourceUsage(it.first, it.second) }
    }
}

data class WorkloadResourceUsage(val workload: Workload, val resourceUsages: List<ResourceUsage>)