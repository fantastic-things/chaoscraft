package dev.strrl.chaoscraft.mod.show

import com.google.common.util.concurrent.RateLimiter
import dev.strrl.chaoscraft.grabber.KubePodsGrabber
import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import dev.strrl.chaoscraft.mod.block.GardenBeaconBlockEntity
import dev.strrl.chaoscraft.mod.show.zone.ApplyZoneActions
import dev.strrl.chaoscraft.mod.show.zone.CompositeZone
import dev.strrl.chaoscraft.mod.show.zone.FenceBorder
import dev.strrl.chaoscraft.mod.show.zone.FlatFloor
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import net.minecraft.entity.Entity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@Suppress("UnstableApiUsage")
class Gardener(
    private val serverWorld: ServerWorld,
    private val beaconBlockPos: BlockPos,
) : CoroutineScope {

    private val rateLimiter = RateLimiter.create(0.5);
    private val actionFactory = ServerWorldsActionFactory(serverWorld)
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
            val actions: MutableList<Action> = mutableListOf()
            syncDataFromKubernetes()

            actions.addAll(cleanupUnexpectedDiedEntities())
            actions.addAll(removeEntitiesForUnexistedWorkloads())

            actions.addAll(preparePlayground())

            actions.addAll(spawnNewEntitiesForNotIntroducedWorkloads())

            actions.forEach { it.run() }
        }
    }

    private fun preparePlayground(): List<Action> {
        val size = 20
        val offset = -size / 2

        val floor = FlatFloor(
            size, size
        ).withOffset(Position(2, -1, offset))

        val fence = FenceBorder(
            size, size
        ).withOffset(Position(2, 0, offset))
        val zone = CompositeZone(listOf(floor, fence))
        return ApplyZoneActions(
            serverWorld, Position.fromBlockPos(beaconBlockPos), zone
        ).actions()
    }

    private fun restoreEntity() {
        // notice that: you could only call the getBlockEntity() method within the thread already registered in this
        // serverWorld.thread
        entityCache = serverWorld.getBlockEntity(beaconBlockPos, ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY).get()
    }

    private fun fetchEntity(): GardenBeaconBlockEntity {
        return entityCache!!
    }

    /**
     * update workloads data from kubernetes
     */
    private fun syncDataFromKubernetes() {
        val originState = fetchEntity().state

        val originWorkloads = originState.workloads
        val newWorkloads = KubePodsGrabber(DefaultKubernetesClient()).listWorkloadsInNamespace("default").toSet()

        if (originWorkloads != newWorkloads) {
            fetchEntity().state = originState.copy(workloads = newWorkloads)
        }
    }

    /**
     * cleanup entities that were killed unexpectedly
     */
    private fun cleanupUnexpectedDiedEntities(): List<Action> {
        val result = mutableListOf<Action>()
        val originState = fetchEntity().state
        val mapped = originState.controlledEntityIds.map { Pair(it, this.serverWorld.getEntity(it)) }

        val notExistedAnymore = mapped.filter { it.second == null }

        val diedUnexpected = mapped.toSet().filter { it.second != null }.filter { !it.second!!.isAlive }
        diedUnexpected.forEach { result.add(actionFactory.removeEntityFromWorld(it.second!!)) }

        val after = originState.controlledEntityIds.toMutableSet()
        after.removeAll(notExistedAnymore.map { it.first }.toSet())
        after.removeAll(diedUnexpected.map { it.first }.toSet())

        fetchEntity().state = originState.copy(controlledEntityIds = after)
        return result
    }

    /**
     * remove entities that are not introduced in the workloads
     */
    private fun removeEntitiesForUnexistedWorkloads(): List<Action> {
        val result: MutableList<Action> = mutableListOf()
        val state = fetchEntity().state
        val entities = state.controlledEntityIds.map { this.serverWorld.getEntity(it)!! }
        val workloads = state.workloads
        val entityNeedToRemove: MutableList<Entity> = mutableListOf()
        val namespacedNameWorkloadMapping = workloads.associateBy { it.namespacedName() }
        for (entity in entities) {
            if (!namespacedNameWorkloadMapping.containsKey(entity.customName?.string ?: "")) {
                entityNeedToRemove.add(entity)
            }
        }
        entityNeedToRemove.stream().peek { result.add(actionFactory.killEntity(it)) }.toList().toSet()

        return result
    }

    /**
     * spawn new entities for new-created workloads
     */
    private fun spawnNewEntitiesForNotIntroducedWorkloads(): List<Action> {
        val result: MutableList<Action> = mutableListOf()
        val state = fetchEntity().state
        val customNameEntityMapping =
            state.controlledEntityIds.map { serverWorld.getEntity(it)!! }.associateBy { it.customName?.string ?: "" }
        val workloadNeedToSpawn = state.workloads.filter { !customNameEntityMapping.containsKey(it.namespacedName()) }

        result.add(
            actionFactory.spawnEntityForWorkloads(
                fetchEntity(), workloadNeedToSpawn, spawnPosition()
            )
        )
        return result
    }

    private fun spawnPosition(): Position {
        return Position(
            this.beaconBlockPos.x + 9, this.beaconBlockPos.y + 1, this.beaconBlockPos.z
        )
    }


    override val coroutineContext: CoroutineContext
        get() = workerPool

}


