package dev.strrl.chaoscraftmod.client

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import org.slf4j.LoggerFactory

/**
 * [PlaygroundBeaconBlock] introduce the base position of chaoscraft playground.
 */
class PlaygroundBeaconBlock(setting: Settings) : Block(setting) {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        logger.debug("PlaygroundBeaconBlock placed at $pos")
    }
}