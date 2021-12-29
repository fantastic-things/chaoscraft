package dev.strrl.chaoscraftmod.client

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


val FABRIC_ITEM = Item(FabricItemSettings().group(ItemGroup.MISC))
val PLAYGROUND_BEACON_BLOCK: Block = PlaygroundBeaconBlock(FabricBlockSettings.of(Material.STONE).hardness(4.0f))

@Suppress("unused")
fun init() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    println("Hello Fabric world!")
    Registry.register(Registry.ITEM, Identifier("tutorial", "fabric_item"), FABRIC_ITEM)
    Registry.register(
        Registry.BLOCK,
        Identifier("chaoscraft", "playground_beacon"),
        PLAYGROUND_BEACON_BLOCK
    )
    Registry.register(
        Registry.ITEM,
        Identifier("chaoscraft", "playground_beacon"),
        BlockItem(
            PLAYGROUND_BEACON_BLOCK,
            Item.Settings().group(ItemGroup.MISC)
        )
    )
}

