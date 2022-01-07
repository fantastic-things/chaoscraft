package dev.strrl.chaoscraft.grabber

import dev.strrl.chaoscraft.api.NetworkTraffic
import dev.strrl.chaoscraft.api.Workload

interface NetworkTrafficGrabber {
    fun listTraffics(): List<NetworkTraffic>
}