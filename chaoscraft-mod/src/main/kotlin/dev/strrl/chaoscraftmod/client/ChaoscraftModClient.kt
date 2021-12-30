package dev.strrl.chaoscraftmod.client

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


val FABRIC_ITEM = Item(FabricItemSettings().group(ItemGroup.MISC))
val FABRIC_BLOCK = DemoBlock(FabricBlockSettings.of(Material.METAL).hardness(4.0f))
var DEMO_BLOCK_ENTITY: BlockEntityType<DemoBlockEntity>? = null

@Suppress("unused")
fun init() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    println("Hello Fabric world!")
    Registry.register(Registry.ITEM, Identifier("tutorial", "fabric_item"), FABRIC_ITEM)

    Registry.register(Registry.BLOCK, Identifier("tutorial", "fabric_block"), FABRIC_BLOCK)
    Registry.register(
        Registry.ITEM, Identifier("tutorial", "fabric_block"), BlockItem(
            FABRIC_BLOCK, Item.Settings().group(
                ItemGroup.MISC
            )
        )
    )
    DEMO_BLOCK_ENTITY = Registry.register(
        Registry.BLOCK_ENTITY_TYPE, "tutorial:demo_block_entity",
        FabricBlockEntityTypeBuilder.create({ pos, state -> DemoBlockEntity(pos, state) }, FABRIC_BLOCK).build(null)
    )

    //BlockEntityRendererRegistry.INSTANCE.register(DEMO_BLOCK_ENTITY, DemoBlockEntityRenderer::new)

    BlockEntityRendererRegistry.register(DEMO_BLOCK_ENTITY, ::DemoBlockEntityRenderer)

}

