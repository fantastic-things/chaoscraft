package dev.strrl.chaoscraft.mod.show

import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

interface Action : Runnable

class ServerWorldsActionFactory(
    private val serverWorld: ServerWorld

) {

    fun spawnEntityForWorkloads(
        gardenBeaconBlockEntity: GardenBeaconBlockEntity,
        workloads: List<Workload>,
        spawnPosition: Position,
    ): Action {
        return object : Action {
            override fun run() {
                val spawnedEntities = mutableListOf<Entity>()
                for (workload in workloads) {
                    val sheep = EntityType.SHEEP.create(serverWorld)!!
                    sheep.setPos(
                        spawnPosition.x.toDouble(),
                        spawnPosition.y.toDouble(),
                        spawnPosition.z.toDouble()
                    )
                    sheep.isCustomNameVisible = true
                    sheep.customName = Text.of(workload.namespacedName())
                    serverWorld.spawnEntity(sheep)
                    spawnedEntities.add(sheep)
                }

                val after = gardenBeaconBlockEntity.state.controlledEntityIds.toMutableSet()
                after.addAll(spawnedEntities.map { it.uuid })
                gardenBeaconBlockEntity.state = gardenBeaconBlockEntity.state.copy(
                    controlledEntityIds = after
                )
            }
        }
    }

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
                entity.remove(Entity.RemovalReason.KILLED)
            }
        }
    }

    fun putBlock(position: Position, block: Block): Action {
        return putBlock(position.toBlockPos(), block)
    }

    fun putBlock(blockPos: BlockPos, block: Block): Action {
        return object : Action {
            override fun run() {
                serverWorld.setBlockState(blockPos, block.defaultState)
            }
        }
    }
}