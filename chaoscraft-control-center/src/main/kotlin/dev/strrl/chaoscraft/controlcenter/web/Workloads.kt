package dev.strrl.chaoscraft.controlcenter.web

import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.grabber.WorkloadGrabber
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/workload")
class Workloads(private val workloadGrabber: WorkloadGrabber) {
    @GetMapping("/list/all")
    fun listWorkloads(): List<Workload> {
        return this.workloadGrabber.listWorkloads()
    }
}