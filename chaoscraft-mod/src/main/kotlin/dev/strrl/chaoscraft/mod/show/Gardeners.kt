package dev.strrl.chaoscraft.mod.show

import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

class Gardeners {
    companion object {

        private val controlledGardeners: MutableMap<Position, Gardener> = mutableMapOf()

        fun acquireForBlock(
            serverWorld: ServerWorld,
            beaconBlockPos: BlockPos,
        ): Gardener {
            return synchronized(
                this
            ) {
                controlledGardeners.computeIfAbsent(
                    Position(beaconBlockPos.x, beaconBlockPos.y, beaconBlockPos.z)
                ) {
                    Gardener(serverWorld, beaconBlockPos)
                }
            }
        }
    }
}