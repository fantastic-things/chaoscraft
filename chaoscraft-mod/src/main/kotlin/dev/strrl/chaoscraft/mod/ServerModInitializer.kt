package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun initServer() {
    Registry.register(
        Registry.BLOCK, Identifier("chaoscraft", "playground_beacon"), ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK
    )
    Registry.register(
        Registry.ITEM, Identifier("chaoscraft", "playground_beacon"), BlockItem(
            ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK, Item.Settings().group(ItemGroup.MISC)
        )
    )
    ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        "chaoscraft:playground_beacon_entity",
        FabricBlockEntityTypeBuilder.create(::GardenBeaconBlockEntity, ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK)
            .build(null)
    )

    ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY = Registry.register(
        Registry.ENTITY_TYPE,
        Identifier("chaoscraft", "workload_sheep"),
        FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            ::WorkloadSheepEntity,
        ).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    )
    FabricDefaultAttributeRegistry.register(
        ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY,
        SheepEntity.createSheepAttributes()
    )
}
