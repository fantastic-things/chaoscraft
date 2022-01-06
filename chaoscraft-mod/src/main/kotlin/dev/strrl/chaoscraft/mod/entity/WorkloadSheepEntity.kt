package dev.strrl.chaoscraft.mod.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class WorkloadSheepEntity(entityType: EntityType<out SheepEntity>?, world: World?) : SheepEntity(
    entityType, world
) {
    override fun isCustomNameVisible(): Boolean {
        return true
    }

    val cpuUsage: Double = 10.0
    val cpuCapacity: Double = 100.0
    val memoryUsage: Double = 10.0
    val memoryCapacity: Double = 100.0

    private val dirty: AtomicBoolean = AtomicBoolean(false)

    var networkCrystal: NetworkCrystalEntity? = null
        get() = field
        set(value) {
            field = value
        }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        val content = nbt.getString(NETWORK_CRYSTAL)
        if (content != "null" && content.isNotEmpty()) {
            val networkCrystalEntityUUID: UUID? = OBJECT_MAPPER.readValue(content)
            this.networkCrystal =
                (this.world as ServerWorld).getEntity(networkCrystalEntityUUID) as NetworkCrystalEntity?
        }
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putString(NETWORK_CRYSTAL, OBJECT_MAPPER.writeValueAsString(this.networkCrystal?.uuid))

    }

    override fun onDeath(source: DamageSource?) {
        if (this.networkCrystal != null) {
            this.networkCrystal?.kill()
            this.networkCrystal?.remove(RemovalReason.DISCARDED)
        }
    }

    companion object {
        val CPU_USAGE = "cpu_usage"
        val CPU_CAPACITY = "cpu_capacity"
        val MEMORY_USAGE = "memory_usage"
        val MEMORY_CAPACITY = "memory_capacity"
        val NETWORK_CRYSTAL = "network_crystal"

        val OBJECT_MAPPER = jacksonObjectMapper()
    }
}