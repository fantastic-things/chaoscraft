package dev.strrl.chaoscraftmod.client

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.LoggerFactory

/**
 * [PlaygroundBeaconBlock] introduce the base position of chaoscraft playground.
 */
class PlaygroundBeaconBlock(setting: Settings) : Block(setting), BlockEntityProvider {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Synchronized
    override fun onPlaced(
        world: World, pos: BlockPos, state: BlockState?, placer: LivingEntity?, itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        logger.debug("PlaygroundBeaconBlock placed at $pos")
        synchronized(this) {
            val y = pos.y - 1
            if (y > 10) {
                for (x in pos.x..pos.x + 5) {
                    for (z in pos.z..pos.z + 5) {
                        val position = BlockPos(x, y, z)
                        world.removeBlock(position, false)

                        if (world.canSetBlock(position)) {
                            val defaultState = PLAYGROUND_BEACON_BLOCK.defaultState
                            world.setBlockState(position, defaultState)
                        }
                    }
                }
            }
        }
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return PlaygroundBeaconBlockEntity(pos, state)
    }
}

class PlaygroundBeaconBlockEntity(
    pos: BlockPos?, state: BlockState?
) : BlockEntity(PLAYGROUND_BEACON_BLOCK_ENTITY, pos, state) {

}