package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntityRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EndCrystalEntityRenderer


@Suppress("unused")
fun initClient() {
    EntityRendererRegistry.register(
        ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY,
        ::WorkloadSheepEntityRenderer
    )

    EntityRendererRegistry.register(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY,
        ::EndCrystalEntityRenderer
    )
}
