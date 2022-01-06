package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntityRenderer
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntityRenderer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry


@Suppress("unused")
fun initClient() {
    EntityRendererRegistry.register(
        ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY,
        ::WorkloadSheepEntityRenderer
    )

    EntityRendererRegistry.register(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY,
        ::NetworkCrystalEntityRenderer
    )
}
