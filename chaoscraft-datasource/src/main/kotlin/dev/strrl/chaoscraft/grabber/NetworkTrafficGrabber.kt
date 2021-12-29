package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.NetworkTraffic
import dev.strrl.chaoscraft.api.Workload

interface NetworkTrafficGrabber {
    fun listTraffics(): List<NetworkTraffic>
    fun listTrafficsAbout(workload: Workload): List<NetworkTraffic>
    fun listTrafficsFrom(workload: Workload): List<NetworkTraffic>
    fun listTrafficsTo(workload: Workload): List<NetworkTraffic>
}