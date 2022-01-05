package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position
import dev.strrl.chaoscraft.mod.show.SimpleBlock

class AirZone(
    private val width: Int = 5,
    private val height: Int = 5,
    private val verticalHeight: Int = 10,
) : Zone {
    override fun expectedBlocks(): List<ExpectedBlock> {
        return (0 until width).flatMap { x ->
            (0 until height).flatMap { z ->
                (0 until verticalHeight).map { y ->
                    ExpectedBlock(
                        Position(x, y, z),
                        SimpleBlock.Air
                    )
                }
            }
        }
    }
}