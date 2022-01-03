package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntityRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry


@Suppress("unused")
fun initClient() {
    EntityRendererRegistry.register(
        ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY,
        ::WorkloadSheepEntityRenderer
    )
}

