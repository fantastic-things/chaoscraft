package dev.strrl.chaoscraft.mod.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class NetworkCrystalEntity(entityType: EntityType<out EndCrystalEntity>?, world: World?) :
    EndCrystalEntity(entityType, world) {

    private var bindingEntity: Entity? = null
    private var floatHeight: Double = 2.0

    constructor(world: World, x: Double, y: Double, z: Double) : this(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY,
        world
    ) {
        this.setPosition(x, y, z)
    }

    constructor(world: World, parent: Entity, floatHeight: Double) : this(
        ChaoscraftEntityType.NETWORK_CRYSTAL_ENTITY,
        world
    ) {
        this.bindingEntity = parent
        this.floatHeight = floatHeight
        this.setShowBottom(false)
        this.setPos(
            parent.x,
            parent.y + floatHeight,
            parent.z
        )
    }

    private fun bindedPosition(): Vec3d {
        if (bindingEntity == null) {
            return Vec3d(this.x, this.y, this.z)
        }
        return Vec3d(bindingEntity!!.pos.x, bindingEntity!!.pos.y + floatHeight, bindingEntity!!.pos.z)
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
}