package dev.strrl.chaoscraft.mod.block

import dev.strrl.chaoscraft.mod.show.Gardeners
import kotlinx.coroutines.Dispatchers
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

/**
 * [GardenBeaconBlock] introduce the base position of chaoscraft playground.
 */
class GardenBeaconBlock(setting: Settings) : Block(setting), BlockEntityProvider {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Synchronized
    override fun onPlaced(
        world: World, pos: BlockPos, state: BlockState?, placer: LivingEntity?, itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        logger.debug("PlaygroundBeaconBlock placed at $pos")
    }

    override fun onUse(
        state: BlockState, world: World, pos: BlockPos, player: PlayerEntity?, hand: Hand?, hit: BlockHitResult?
    ): ActionResult {
        if (world.isClient) {
            return ActionResult.SUCCESS
        }
        return ActionResult.SUCCESS
    }

    override fun neighborUpdate(
        state: BlockState?, world: World, pos: BlockPos, block: Block, fromPos: BlockPos, notify: Boolean
    ) {
        val redstoneSignal = world.isReceivingRedstonePower(pos)
        if (redstoneSignal) {
            onRedstoneSignal(world, pos)
        }
    }

    private fun onRedstoneSignal(world: World, pos: BlockPos) {
        logger.debug("onRedstoneSignal, world: {}, position: {}", world, pos)
        if (!world.isClient) {
            Gardeners.acquireForBlock(
                world as ServerWorld,
                pos,
            ).work()
        }
    }

    override fun onBreak(world: World, pos: BlockPos, state: BlockState?, player: PlayerEntity?) {
        super.onBreak(world, pos, state, player)
        logger.debug("PlaygroundBeaconBlock broken at $pos")
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GardenBeaconBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World?, state: BlockState?, type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        return super.getTicker(world, state, type)
    }
}

