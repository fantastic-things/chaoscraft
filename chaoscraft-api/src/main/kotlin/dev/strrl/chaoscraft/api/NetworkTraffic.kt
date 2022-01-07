package dev.strrl.chaoscraft.api

data class NetworkTraffic(
    val from: Workload,
    val to: Workload,
    val packets: Int,
)
