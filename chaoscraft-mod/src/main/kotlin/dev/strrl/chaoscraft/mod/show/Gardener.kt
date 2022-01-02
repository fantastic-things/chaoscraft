package dev.strrl.chaoscraft.mod.show

import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.grabber.KubePodsGrabber
import io.fabric8.kubernetes.client.DefaultKubernetesClient
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.text.Text

class Gardener(private val garden: Garden) {


    fun syncData() {
        garden.workloads.clear()
        garden.workloads.addAll(KubePodsGrabber(DefaultKubernetesClient()).listWorkloadsInNamespace("default"))

        val entitiesToRemove = garden.entities.filter { !it.isAlive }
        entitiesToRemove.forEach(garden.entities::remove)
    }

    fun prepareGarden() {
        var workloadNeedToSpawn: MutableList<Workload> = mutableListOf()
        var entityNeedToRemove: MutableList<Entity> = mutableListOf()

        val customNameEntityMapping = garden.entities.associateBy { it.customName?.string ?: "" }
        for (workload in garden.workloads) {
            if (!customNameEntityMapping.containsKey(workload.namespacedName())) {
                workloadNeedToSpawn.add(workload)
            }
        }
        val namespacedNameWorkloadMapping = garden.workloads.associateBy { it.namespacedName() }
        for (entity in garden.entities) {
            if (!namespacedNameWorkloadMapping.containsKey(entity.customName?.string ?: "")) {
                entityNeedToRemove.add(entity)
            }
        }

        for (workload in workloadNeedToSpawn) {
            spawnWorkload(workload)
        }
        for (entity in entityNeedToRemove) {
            entity.kill()
        }
    }

    fun spawnWorkload(workload: Workload) {
        val sheep = EntityType.SHEEP.create(garden.world)!!
        sheep.setPos(garden.base.x.toDouble(), (garden.base.y + 5).toDouble(), garden.base.z.toDouble())
        sheep.isCustomNameVisible = true
        sheep.customName = Text.of(workload.namespacedName())
        garden.world.spawnEntity(sheep)
        garden.entities.add(sheep)
    }

    fun removeEntity(entity: Entity) {
        entity.remove(Entity.RemovalReason.DISCARDED)
    }

}