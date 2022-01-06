package dev.strrl.chaoscraft.mod.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
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
    }

    private var bindedEntity: UUID? = null
    private var floatHeight: Float = 2.0f
    private val beamTargets: MutableList<UUID> = mutableListOf()

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
        this.bindedEntity = entity.uuid
    }

    fun fetchBindedEntity(): Optional<Entity> {
        if (this.bindedEntity == null) {
            return Optional.empty()
        }
        return Optional.ofNullable((this.world as ServerWorld).getEntity(this.bindedEntity))
    }

    fun updateBeamTargets(targets: List<Entity>) {
        this.beamTargets.clear()
        this.beamTargets.addAll(targets.map { it.uuid })
    }

    fun fetchBeamTargets(): List<Entity> {
        return this.beamTargets.map { (this.world as ServerWorld).getEntity(it) }.filterNotNull()
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

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putString(BINDED_ENTITY, OBJECT_MAPPER.writeValueAsString(this.bindedEntity))
        nbt.putString(BEAM_TARGETS, OBJECT_MAPPER.writeValueAsString(this.beamTargets))
        nbt.putFloat(FLOAT_HEIGHT, this.floatHeight)
        return super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound) {
        this.bindedEntity = OBJECT_MAPPER.readValue(nbt.getString(BINDED_ENTITY))
        val entities: List<UUID> = OBJECT_MAPPER.readValue(nbt.getString(BEAM_TARGETS))
        this.beamTargets.addAll(entities)
        this.floatHeight = nbt.getFloat(FLOAT_HEIGHT)
        super.readNbt(nbt)
    }
}