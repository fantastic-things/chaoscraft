package dev.strrl.chaoscraftmod.client

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


val FABRIC_ITEM = Item(FabricItemSettings().group(ItemGroup.MISC))

@Suppress("unused")
fun init() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    println("Hello Fabric world!")
    Registry.register(Registry.ITEM, Identifier("tutorial", "fabric_item"), FABRIC_ITEM)
}

