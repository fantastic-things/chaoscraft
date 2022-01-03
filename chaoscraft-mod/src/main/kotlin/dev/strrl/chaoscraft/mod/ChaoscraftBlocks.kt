package dev.strrl.chaoscraft.mod

import dev.strrl.chaoscraft.mod.block.GardenBeaconBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material

class ChaoscraftBlocks {
    companion object {
        val PLAYGROUND_BEACON_BLOCK: Block =
            GardenBeaconBlock(FabricBlockSettings.of(Material.STONE).hardness(2.0f))
    }
}