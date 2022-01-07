package dev.strrl.chaoscraft.mod.show

import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntity
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

interface Action : Runnable

class ServerWorldsActionFactory(
    private val serverWorld: ServerWorld

) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun spawnEntityForWorkloads(
        gardenBeaconBlockEntity: GardenBeaconBlockEntity,
        workloads: List<Workload>,
        spawnPosition: Position,
    ): Action {
        return object : Action {
            override fun run() {
                val spawnedEntities = mutableListOf<Entity>()

                for (workload in workloads) {
                    val sheep = ChaoscraftEntityType.WORKLOAD_SHEEP_ENTITY!!.create(serverWorld)!!
                    sheep.setPos(
                        spawnPosition.x.toDouble() + Random.nextDouble(-3.0, 3.0),
                        spawnPosition.y.toDouble() + Random.nextDouble(0.0, 1.0),
                        spawnPosition.z.toDouble() + Random.nextDouble(-3.0, 3.0)
                    )
                    sheep.isCustomNameVisible = true
                    sheep.customName = Text.of(workload.namespacedName())
                    serverWorld.spawnEntity(sheep)
                    spawnedEntities.add(sheep)
                    val networkCrystal = NetworkCrystalEntity(serverWorld, sheep, Random.nextDouble(2.0, 10.0).toFloat())
                    val succeed = serverWorld.spawnEntity(networkCrystal)
                    if (!succeed) {
                        logger.warn("Failed to spawn network crystal for workload ${workload.namespacedName()}")
                    }
                    sheep.updateNetworkCrystal(networkCrystal)
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

    fun setBlock(blockPos: BlockPos, block: Block): Action {
        return object : Action {
            override fun run() {
                val succeed = serverWorld.setBlockState(blockPos, block.defaultState)
                if (!succeed) {
                    logger.debug("Failed to set block at $blockPos, target block is $block")
                }
            }
        }
    }
}