package dev.strrl.chaoscraft.mod.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.EntityDamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

class NetworkCrystalEntity(entityType: EntityType<out EndCrystalEntity>?, world: World?) :
    EndCrystalEntity(entityType, world) {

    companion object {
        private val OBJECT_MAPPER = jacksonObjectMapper()
        private const val BINDED_ENTITY = "binded_entity"
        private const val BEAM_TARGETS = "beam_targets"
        private const val FLOAT_HEIGHT = "float_height"
        private const val NETWORK_DISABLED = "network_disabled"

        private val TRACKED_BEAM_TARGETS =
            DataTracker.registerData(NetworkCrystalEntity::class.java, TrackedDataHandlerRegistry.STRING)
        private val TRACKED_BINDED_ENTITY =
            DataTracker.registerData(NetworkCrystalEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
        private val TRACKED_FLOAT_HEIGHT =
            DataTracker.registerData(NetworkCrystalEntity::class.java, TrackedDataHandlerRegistry.FLOAT)
        private val TRACKED_NETWORK_DISABLED =
            DataTracker.registerData(NetworkCrystalEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
    }

    constructor(world: World, x: Double, y: Double, z: Double) : this(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY, world
    ) {
        this.setPosition(x, y, z)
    }

    constructor(world: World, parent: Entity, floatHeight: Float = 2.0f) : this(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY, world
    ) {
        this.updateBindedEntity(parent)
        this.setShowBottom(false)
        this.updateFloatHeight(floatHeight)
        this.setPos(
            parent.x,
            parent.y + floatHeight,
            parent.z
        )
    }

    private fun updateFloatHeight(floatHeight: Float) {
        this.getDataTracker().set(TRACKED_FLOAT_HEIGHT, floatHeight)
    }

    private fun fetchFloatHeight(): Float {
        return this.getDataTracker().get(TRACKED_FLOAT_HEIGHT)
    }

    override fun initDataTracker() {
        super.initDataTracker()
        this.getDataTracker().startTracking(TRACKED_BEAM_TARGETS, "[]")
        this.getDataTracker().startTracking(TRACKED_BINDED_ENTITY, 0)
        this.getDataTracker().startTracking(TRACKED_FLOAT_HEIGHT, 2.0f)
        this.getDataTracker().startTracking(TRACKED_NETWORK_DISABLED, false)
    }

    private fun bindedPosition(): Vec3d {
        val entityOptional = fetchBindedEntity()
        return if (entityOptional.isPresent) {
            val entity = entityOptional.get()
            Vec3d(entity.x, entity.y + this.fetchFloatHeight(), entity.z)
        } else {
            Vec3d(this.x, this.y, this.z)
        }
    }


    fun updateBindedEntity(entity: Entity) {
        this.getDataTracker().set(TRACKED_BINDED_ENTITY, entity.id)
    }

    fun fetchBindedEntity(): Optional<Entity> {
        val entityId = this.getDataTracker().get(TRACKED_BINDED_ENTITY)
        if (entityId <= 0) {
            return Optional.empty()
        }
        return Optional.ofNullable(this.world.getEntityById(entityId))
    }

    fun updateBeamTargets(targets: List<Entity>) {
        this.getDataTracker().set(TRACKED_BEAM_TARGETS, OBJECT_MAPPER.writeValueAsString(targets.map { it.id }))
    }

    fun fetchBeamTargets(): List<Entity> {
        val ids: List<Int> = OBJECT_MAPPER.readValue(this.getDataTracker().get(TRACKED_BEAM_TARGETS))
        return ids.mapNotNull { this.world.getEntityById(it) }
    }

    fun fetchNetworkDisabled(): Boolean {
        return this.getDataTracker().get(TRACKED_NETWORK_DISABLED)
    }

    fun updateNetworkDisabled(v: Boolean) {
        this.getDataTracker().set(TRACKED_NETWORK_DISABLED, v)
    }

    override fun getPos(): Vec3d {
        val expected = this.bindedPosition()
        val actual = Vec3d(this.x, this.y, this.z)
        if (actual != expected) {
            this.setPosition(expected.x, expected.y, expected.z)
        }
        return super.getPos()
    }

    override fun damage(source: DamageSource?, amount: Float): Boolean {
        if (source is EntityDamageSource) {
            if (source.attacker is PlayerEntity) {
                val player = source.attacker as PlayerEntity
                if (player.offHandStack.item == Items.DIAMOND_SWORD) {
                    return true
                }
            }
        }

        this.kill()
        return true
    }

    override fun kill() {
        this.remove(RemovalReason.KILLED)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putString(BEAM_TARGETS, OBJECT_MAPPER.writeValueAsString(this.fetchBeamTargets().map { it.id }))
        nbt.putInt(BINDED_ENTITY, this.getDataTracker().get(TRACKED_BINDED_ENTITY))
        nbt.putFloat(FLOAT_HEIGHT, this.fetchFloatHeight())
        nbt.putBoolean(NETWORK_DISABLED, this.getDataTracker().get(TRACKED_NETWORK_DISABLED))
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        val entityIDs: List<Int> = OBJECT_MAPPER.readValue(nbt.getString(BEAM_TARGETS))
        this.updateBeamTargets(entityIDs.mapNotNull { this.world.getEntityById(it) })
        this.getDataTracker().set(TRACKED_BINDED_ENTITY, nbt.getInt(BINDED_ENTITY))
        this.updateFloatHeight(nbt.getFloat(FLOAT_HEIGHT))
        this.getDataTracker().set(TRACKED_NETWORK_DISABLED, nbt.getBoolean(NETWORK_DISABLED))
    }

}