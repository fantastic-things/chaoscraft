package dev.strrl.chaoscraft.mod.show

import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld

interface Action : Runnable {
}

class ServerWorldsActionFactory(
    private val serverWorld: ServerWorld

) {

    fun killEntity(entity: Entity): Action {
        return object : Action {
            override fun run() {
                entity.kill()
            }
        }
    }

    fun removeEntityFromWorld(entity: Entity): Action {
        return object : Action {
            override fun run() {
                entity.remove(Entity.RemovalReason.DISCARDED)
            }
        }
    }
}