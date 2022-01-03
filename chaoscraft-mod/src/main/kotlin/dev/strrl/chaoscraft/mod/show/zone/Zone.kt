package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position

interface Zone {
    fun expectedBlocks(): List<ExpectedBlock>

    fun withOffset(offset: Position): Zone {
        return OffsetDecorator(this, offset)
    }
}