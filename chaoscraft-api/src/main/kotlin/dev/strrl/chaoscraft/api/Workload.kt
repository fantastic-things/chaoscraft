package dev.strrl.chaoscraft.api

data class Workload(
    val namespace: String = "",
    val name: String = "",
) {
    fun namespacedName(): String {
        return "$namespace/$name"
    }
}
