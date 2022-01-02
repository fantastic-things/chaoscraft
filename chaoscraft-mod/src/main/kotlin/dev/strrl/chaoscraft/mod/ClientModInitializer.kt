package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.block.GardenBeaconBlock
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


@Suppress("unused")
fun initClient() {
    Registry.register(
        Registry.BLOCK,
        Identifier("chaoscraft", "playground_beacon"),
        ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK
    )
    Registry.register(
        Registry.ITEM,
        Identifier("chaoscraft", "playground_beacon"),
        BlockItem(
            ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK,
            Item.Settings().group(ItemGroup.MISC)
        )
    )
    ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE,
        "chaoscraft:playground_beacon_entity",
        FabricBlockEntityTypeBuilder.create(::GardenBeaconBlockEntity, ChaoscraftBlocks.PLAYGROUND_BEACON_BLOCK)
            .build(null)
    )
}

class ChaoscraftBlocks {
    companion object {
        val PLAYGROUND_BEACON_BLOCK: Block =
            GardenBeaconBlock(FabricBlockSettings.of(Material.STONE).hardness(2.0f))
    }
}

class ChaoscraftEntityType {
    companion object {
        var GARDEN_BEACON_BLOCK_ENTITY: BlockEntityType<GardenBeaconBlockEntity>? = null
    }

}