package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position
import dev.strrl.chaoscraft.mod.show.SimpleBlock

class FlatFloor(
    private val width: Int = 5,
    private val height: Int = 5,
    private val material: SimpleBlock = SimpleBlock.Grass,
) : Zone {
    override fun expectedBlocks(): List<ExpectedBlock> {
        return (0 until width).flatMap { x ->
            (0 until height).map { y ->
                ExpectedBlock(Position(x, 0, y), material)
            }
        }
    }
}