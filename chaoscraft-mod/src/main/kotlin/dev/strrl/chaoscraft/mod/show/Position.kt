package dev.strrl.chaoscraft.mod.show

import net.minecraft.util.math.BlockPos

data class Position(val x: Int, val y: Int, val z: Int) {
    fun toBlockPos(): BlockPos {
        return BlockPos(x, y, z)
    }

    fun relativeToAbsolute(base: Position): Position {
        return withOffset(base)
    }

    fun withOffset(offset: Position): Position {
        return Position(x + offset.x, y + offset.y, z + offset.z)
    }

    companion object {
        fun fromBlockPos(pos: BlockPos): Position {
            return Position(pos.x, pos.y, pos.z)
        }
    }
}
