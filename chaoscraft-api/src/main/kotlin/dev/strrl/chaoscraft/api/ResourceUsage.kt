package dev.strrl.chaoscraft.api

data class ResourceUsage(
    val usage: Double,
    val capacity: Double,
    val resourceType: ResourceType,
)

enum class ResourceType {
    CPU,
    Memory,
    Disk,
}