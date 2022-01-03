package dev.strrl.chaoscraft.mod.show.zone

import dev.strrl.chaoscraft.mod.show.Action
import dev.strrl.chaoscraft.mod.show.ExpectedBlock
import dev.strrl.chaoscraft.mod.show.Position
import dev.strrl.chaoscraft.mod.show.ServerWorldsActionFactory
import net.minecraft.server.world.ServerWorld

class ApplyZoneActions(
    private val serverWorld: ServerWorld,
    private val basePosition: Position,
    private val zone: Zone,
) {
    fun actions(): List<Action> {
        val diff = mutableListOf<ExpectedBlock>()
        for (expectedBlock in zone.expectedBlocks()) {
            val targetPosition = expectedBlock.position.relativeToAbsolute(basePosition)
            val existedBlock = serverWorld.getBlockState(targetPosition.toBlockPos())!!
            if (existedBlock.block != expectedBlock.type.toBlock()) {
                diff.add(expectedBlock)
            }
        }
        val factory = ServerWorldsActionFactory(serverWorld)
        return diff.map {
            factory.setBlock(
                it.position.relativeToAbsolute(basePosition).toBlockPos(),
                it.type.toBlock()
            )
        }
    }
}