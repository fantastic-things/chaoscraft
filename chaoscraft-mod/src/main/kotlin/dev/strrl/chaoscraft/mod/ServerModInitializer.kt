package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntity
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntity
import dev.strrl.chaoscraft.mod.item.ChaoscraftItems
import dev.strrl.chaoscraft.mod.item.NetworkCrystalItem
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

private const val CHAOSCRAFT = "chaoscraft"

fun initServer() {
    Registry.register(
        Registry.BLOCK, Identifier(CHAOSCRAFT, "playground_beacon"), ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK
    )

    ChaoscraftItems.PLAYGROUND_BEACON_BLOCK = Registry.register(
        Registry.ITEM, Identifier(CHAOSCRAFT, "playground_beacon"), BlockItem(
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
        Registry.ENTITY_TYPE, Identifier(CHAOSCRAFT, "workload_sheep"), FabricEntityTypeBuilder.create(
            SpawnGroup.CREATURE,
            ::WorkloadSheepEntity,
        ).dimensions(EntityDimensions.fixed(0.9f, 1.3f)).build()
    )
    FabricDefaultAttributeRegistry.register(
        ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY, SheepEntity.createSheepAttributes()
    )

    ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY = Registry.register(
        Registry.ENTITY_TYPE, Identifier(CHAOSCRAFT, "network_crystal"), FabricEntityTypeBuilder.create(
            SpawnGroup.MISC,
            ::NetworkCrystalEntity,
        ).dimensions(EntityDimensions.fixed(2.0f, 2.0f)).build()
    )

    ChaoscraftItems.NETWORK_CRYSTAL = Registry.register(
        Registry.ITEM,
        Identifier(CHAOSCRAFT, "network_crystal"),
        NetworkCrystalItem(Item.Settings().group(ItemGroup.MISC))
    )

}
