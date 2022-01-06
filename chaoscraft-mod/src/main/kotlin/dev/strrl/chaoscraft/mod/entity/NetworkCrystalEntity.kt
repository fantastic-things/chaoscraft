package dev.strrl.chaoscraft.mod.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
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
        private val TRACKED_BEAM_TARGETS =
            DataTracker.registerData(NetworkCrystalEntity::class.java, TrackedDataHandlerRegistry.STRING)
    }

    private var bindedEntity: Entity? = null
    private var floatHeight: Float = 2.0f

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
        this.floatHeight = floatHeight
    }

    override fun initDataTracker() {
        super.initDataTracker()
        this.getDataTracker().startTracking(TRACKED_BEAM_TARGETS, "[]")
    }

    private fun bindedPosition(): Vec3d {
        val entityOptional = fetchBindedEntity()
        return if (entityOptional.isPresent) {
            val entity = entityOptional.get()
            Vec3d(entity.x, entity.y + this.floatHeight, entity.z)
        } else {
            Vec3d(this.x, this.y, this.z)
        }
    }

    fun updateBindedEntity(entity: Entity) {
        this.bindedEntity = entity
    }

    fun fetchBindedEntity(): Optional<Entity> {
        return Optional.ofNullable(this.bindedEntity)
    }

    fun updateBeamTargets(targets: List<Entity>) {
        this.getDataTracker().set(TRACKED_BEAM_TARGETS, OBJECT_MAPPER.writeValueAsString(targets.map { it.id }))
    }

    fun fetchBeamTargets(): List<Entity> {
        val ids: List<Int> = OBJECT_MAPPER.readValue(this.getDataTracker().get(TRACKED_BEAM_TARGETS))
        return ids.mapNotNull { this.world.getEntityById(it) }
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
        this.kill()
        return true
    }

    override fun kill() {
        this.remove(RemovalReason.KILLED)
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        super.readCustomDataFromNbt(nbt)
        val content = nbt.getString(BINDED_ENTITY)
        if (content != "null" && content.isNotEmpty()) {
            val bindedEntityUUID: UUID? = OBJECT_MAPPER.readValue(content)
            this.bindedEntity = (this.world as ServerWorld).getEntity(bindedEntityUUID)
        }
        val entityIDs: List<Int> = OBJECT_MAPPER.readValue(nbt.getString(BEAM_TARGETS))
        this.updateBeamTargets(entityIDs.mapNotNull { this.world.getEntityById(it) })
        this.floatHeight = nbt.getFloat(FLOAT_HEIGHT)
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        super.writeCustomDataToNbt(nbt)
        nbt.putString(BINDED_ENTITY, OBJECT_MAPPER.writeValueAsString(this.bindedEntity?.uuid))
        nbt.putString(BEAM_TARGETS, OBJECT_MAPPER.writeValueAsString(this.fetchBeamTargets().map { it.id }))
        nbt.putFloat(FLOAT_HEIGHT, this.floatHeight)
    }
}