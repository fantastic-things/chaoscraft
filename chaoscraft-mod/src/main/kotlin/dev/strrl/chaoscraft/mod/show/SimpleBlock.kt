package dev.strrl.chaoscraft.mod.show

import net.minecraft.block.Block
import net.minecraft.block.Blocks

enum class SimpleBlock {
    Air, Grass, Fence, Stone;

    fun toBlock(): Block {
        return when (this) {
            Air -> Blocks.AIR
            Grass -> Blocks.GRASS_BLOCK
            Fence -> Blocks.BIRCH_FENCE
            Stone -> Blocks.STONE
        }
    }
}
