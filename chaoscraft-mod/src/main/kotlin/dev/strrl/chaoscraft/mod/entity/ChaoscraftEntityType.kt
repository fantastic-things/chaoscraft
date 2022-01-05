package dev.strrl.chaoscraft.mod.entity

import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.EntityType

class ChaoscraftEntityType {
    companion object {
        var GARDEN_BEACON_BLOCK_ENTITY: BlockEntityType<GardenBeaconBlockEntity>? = null
        var WORKLOAD_SHEEP_ENTITY: EntityType<WorkloadSheepEntity>? = null
        var NETWORK_CRYSTAL_ENTITY: EntityType<NetworkCrystalEntity>? = null
    }
}