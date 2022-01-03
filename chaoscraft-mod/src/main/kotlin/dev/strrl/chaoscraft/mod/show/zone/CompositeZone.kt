package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.ExpectedBlock

class CompositeZone(
    private val zones: List<Zone> = emptyList()
) : Zone {
    override fun expectedBlocks(): List<ExpectedBlock> {
        return zones.flatMap { it.expectedBlocks() }
    }
}