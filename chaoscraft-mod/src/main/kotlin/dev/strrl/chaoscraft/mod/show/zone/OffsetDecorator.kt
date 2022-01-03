package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position

class OffsetDecorator(
    private val origin: Zone,
    private val offset: Position,
) : Zone {
    override fun expectedBlocks(): List<ExpectedBlock> {
        return origin.expectedBlocks().map { ExpectedBlock(it.position.withOffset(offset), it.type) }
    }
}