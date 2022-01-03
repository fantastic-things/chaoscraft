package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position
import dev.strrl.chaoscraft.mod.show.SimpleBlock

class FenceBorder(
    private val width: Int = 5,
    private val height: Int = 5,
) : Zone {
    private fun isBorder(x: Int, y: Int): Boolean {
        return x == 0 || x == width - 1 || y == 0 || y == height - 1
    }

    override fun expectedBlocks(): List<ExpectedBlock> {
        val result = mutableListOf<ExpectedBlock>()
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (isBorder(x, y)) {
                    result.add(ExpectedBlock(Position(x, 0, y), SimpleBlock.Fence))
                }
            }
        }
        return result
    }
}