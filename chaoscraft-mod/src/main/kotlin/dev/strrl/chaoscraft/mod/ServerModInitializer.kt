package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.armor.ChaoscraftArmors
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntity
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntity
import dev.strrl.chaoscraft.mod.item.ChaoscraftItems
import dev.strrl.chaoscraft.mod.item.NetworkCrystalItem
import dev.strrl.chaoscraftmod.client.FABRIC_BLOCK
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.item.*
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

private const val CHAOSCRAFT = "chaoscraft"

val CHAOSCRAFT_MOD_GROUP: ItemGroup = FabricItemGroupBuilder.create(Identifier("chaoscraft", "items"))
    .icon { ItemStack(Items.TNT) }
    .build()

fun initServer() {
    Registry.register(
        Registry.BLOCK, Identifier(CHAOSCRAFT, "garden_beacon_block"), ChaoscraftBlocks.GARDEN_BEACON_BLOCK
    )

    ChaoscraftItems.GARDEN_BEACON_BLOCK = Registry.register(
        Registry.ITEM, Identifier(CHAOSCRAFT, "garden_beacon_block"), BlockItem(
            ChaoscraftBlocks.GARDEN_BEACON_BLOCK, Item.Settings().group(CHAOSCRAFT_MOD_GROUP)
        )
    )
    ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        "chaoscraft:garden_beacon_entity",
        FabricBlockEntityTypeBuilder.create(::GardenBeaconBlockEntity, ChaoscraftBlocks.GARDEN_BEACON_BLOCK)
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

    Registry.register(
        Registry.ITEM,
        Identifier(CHAOSCRAFT, "resource_usage_glass"),
        ChaoscraftArmors.RESOURCE_USAGE_GLASS
    )
    Registry.register(
        Registry.ITEM,
        Identifier(CHAOSCRAFT, "crystal_glass"),
        ChaoscraftArmors.CRYSTAL_GLASS
    )
}
