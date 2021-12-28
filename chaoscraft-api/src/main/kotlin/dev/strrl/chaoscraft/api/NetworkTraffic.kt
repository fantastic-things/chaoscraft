package dev.strrl.chaoscraft.api

data class NetworkTraffic(
    val from: Workload,
    val to: Workload,
    /**
     * The unit of [bandwidth] is bytes per second.
     */
    val bandwidth: Int,
)
