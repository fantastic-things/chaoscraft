package dev.strrl.chaoscraft.mod.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.strrl.chaoscraft.chaos.PodKill
import net.minecraft.entity.EntityType
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World
import java.util.*

class WorkloadSheepEntity(entityType: EntityType<out SheepEntity>?, world: World?) : SheepEntity(
    entityType, world
) {


    override fun isCustomNameVisible(): Boolean {
        return true
    }

    val cpuUsage: Double = Random().nextDouble(10.0, 30.0)
    val cpuCapacity: Double = 100.0
    val memoryUsage: Double = Random().nextDouble(40.0, 80.0)
    val memoryCapacity: Double = 100.0

    override fun initDataTracker() {
        super.initDataTracker()
        this.getDataTracker().startTracking(NETWORK_CRYSTAL_ENTITY_ID, 0)
    }

    fun fetchNetworkCrystal(): Optional<NetworkCrystalEntity> {
        val id = this.getDataTracker().get(NETWORK_CRYSTAL_ENTITY_ID)
        if (id <= 0) {
            return Optional.empty()
        }
        return Optional.ofNullable(this.world.getEntityById(id) as NetworkCrystalEntity?)
    }

    fun updateNetworkCrystal(crystal: NetworkCrystalEntity) {
        this.getDataTracker().set(NETWORK_CRYSTAL_ENTITY_ID, crystal.id)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        this.getDataTracker().set(NETWORK_CRYSTAL_ENTITY_ID, nbt.getInt(NETWORK_CRYSTAL))
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putInt(NETWORK_CRYSTAL, this.getDataTracker().get(NETWORK_CRYSTAL_ENTITY_ID))
    }

    override fun onDeath(source: DamageSource?) {
        this.fetchNetworkCrystal().ifPresent {
            it.kill()
            it.remove(RemovalReason.DISCARDED)
        }
    }

    override fun damage(source: DamageSource, amount: Float): Boolean {
        PodKill.killPod(
            fetchWorkloadNamespace(),
            fetchWorkloadName()
        )
        return super.damage(source, amount)
    }

    fun fetchWorkloadNamespace(): String {
        return customName!!.string.split("/")[0]
    }

    fun fetchWorkloadName(): String {
        return customName!!.string.split("/")[1]
    }


    companion object {
        val CONST_HEALTH = 999.99f
        val CPU_USAGE = "cpu_usage"
        val CPU_CAPACITY = "cpu_capacity"
        val MEMORY_USAGE = "memory_usage"
        val MEMORY_CAPACITY = "memory_capacity"
        val NETWORK_CRYSTAL = "network_crystal"

        val OBJECT_MAPPER = jacksonObjectMapper()
        val NETWORK_CRYSTAL_ENTITY_ID = DataTracker.registerData(
            WorkloadSheepEntity::class.java,
            TrackedDataHandlerRegistry.INTEGER
        )

        fun createWorkloadSheepSheepAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, CONST_HEALTH.toDouble())
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
        }
    }
}