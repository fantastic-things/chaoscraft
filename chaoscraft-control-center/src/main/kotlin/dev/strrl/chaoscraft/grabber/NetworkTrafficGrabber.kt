package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.NetworkTraffic
import dev.strrl.chaoscraft.api.Workload

interface NetworkTrafficGrabber {
    fun allTraffics(): List<NetworkTraffic>
    fun trafficsAbout(workload: Workload): List<NetworkTraffic>
    fun trafficsFrom(workload: Workload): List<NetworkTraffic>
    fun trafficsTo(workload: Workload): List<NetworkTraffic>
}