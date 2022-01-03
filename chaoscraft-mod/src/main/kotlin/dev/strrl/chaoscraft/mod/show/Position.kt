package dev.strrl.chaoscraft.mod.show

import net.minecraft.util.math.BlockPos

data class Position(val x: Int, val y: Int, val z: Int) {
    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }
}
