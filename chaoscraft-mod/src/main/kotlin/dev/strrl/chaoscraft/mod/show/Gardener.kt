package dev.strrl.chaoscraft.mod.show

import com.google.common.util.concurrent.RateLimiter
import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.grabber.KubePodsGrabber
import dev.strrl.chaoscraft.mod.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@Suppress("UnstableApiUsage")
class Gardener(
    private val serverWorld: ServerWorld,
    private val beaconBlockPos: BlockPos,
) : CoroutineScope {

    private val rateLimiter = RateLimiter.create(0.5);
    private var entityCache: GardenBeaconBlockEntity? = null

    companion object {
        private val workerPool = Executors.newWorkStealingPool().asCoroutineDispatcher()
    }

    /**
     * main entry of the gardener
     */
    fun work() {
        if (rateLimiter.tryAcquire()) {
            restoreEntity()

            launch {
                syncDataFromKubernetes()
                cleanupUnexpectedDiedEntities()
                prepareGarden()
            }
        }
    }

    private fun restoreEntity() {
        // notice that: you could only call the getBlockEntity() method within the thread already registered in this
        // serverWorld.thread
        entityCache = serverWorld.getBlockEntity(beaconBlockPos, ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY).get()
    }

    private fun fetchEntity(): GardenBeaconBlockEntity {
        return entityCache!!
    }

    private fun cleanupUnexpectedDiedEntities() {
        val originState = fetchEntity().state
        val mapped = originState.controlledEntityIds.map { Pair(it, this.serverWorld.getEntity(it)) }

        val notExistedAnymore = mapped.filter { it.second == null }

        val diedUnexpected = mapped.toSet().filter { it.second != null }.filter { !it.second!!.isAlive }
        diedUnexpected.forEach { removeEntity(it.second!!) }

        val after = originState.controlledEntityIds.toMutableSet()
        after.removeAll(notExistedAnymore.map { it.first }.toSet())
        after.removeAll(diedUnexpected.map { it.first }.toSet())

        fetchEntity().state = originState.copy(controlledEntityIds = after)
    }

    private fun syncDataFromKubernetes() {
        val originState = fetchEntity().state

        val originWorkloads = originState.workloads
        val newWorkloads = KubePodsGrabber(DefaultKubernetesClient()).listWorkloadsInNamespace("default").toSet()

        if (originWorkloads != newWorkloads) {
            fetchEntity().state = originState.copy(workloads = newWorkloads)
        }
    }

    private fun prepareGarden() {
        val state = fetchEntity().state
        val entities = state.controlledEntityIds.map { this.serverWorld.getEntity(it)!! }
        val workloads = state.workloads

        val workloadNeedToSpawn: MutableList<Workload> = mutableListOf()
        val entityNeedToRemove: MutableList<Entity> = mutableListOf()

        val customNameEntityMapping = entities.associateBy { it.customName?.string ?: "" }
        for (workload in workloads) {
            if (!customNameEntityMapping.containsKey(workload.namespacedName())) {
                workloadNeedToSpawn.add(workload)
            }
        }
        val namespacedNameWorkloadMapping = workloads.associateBy { it.namespacedName() }
        for (entity in entities) {
            if (!namespacedNameWorkloadMapping.containsKey(entity.customName?.string ?: "")) {
                entityNeedToRemove.add(entity)
            }
        }

        val newSpawnedControllerEntities = workloadNeedToSpawn.map {
            spawnWorkload(it)
        }.toSet()
        val killedEntities = entityNeedToRemove.stream().peek { it.kill() }.peek { removeEntity(it) }.toList().toSet()
        val newEntities = entities.toMutableSet()
        newEntities.removeAll(killedEntities)
        newEntities.addAll(newSpawnedControllerEntities)
        fetchEntity().state = state.copy(controlledEntityIds = newEntities.map { it.uuid }.toSet())
    }

    private fun spawnWorkload(workload: Workload): Entity {
        val sheep = EntityType.SHEEP.create(serverWorld)!!
        sheep.setPos(
            this.beaconBlockPos.x.toDouble(), ((this.beaconBlockPos.y + 3).toDouble()), this.beaconBlockPos.z.toDouble()
        )
        sheep.isCustomNameVisible = true
        sheep.customName = Text.of(workload.namespacedName())
        serverWorld.spawnEntity(sheep)
        return sheep
    }

    fun removeEntity(entity: Entity) {
        entity.remove(Entity.RemovalReason.DISCARDED)
    }

    override val coroutineContext: CoroutineContext
        get() = workerPool

}


