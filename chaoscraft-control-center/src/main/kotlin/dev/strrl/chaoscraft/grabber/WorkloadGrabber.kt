package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.Workload

interface WorkloadGrabber {
    fun allWorkloads(): List<Workload>
}