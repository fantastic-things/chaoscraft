package dev.strrl.chaoscraft.mod.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.world.World

class WorkloadSheepEntity(entityType: EntityType<out SheepEntity>?, world: World?) : SheepEntity(
    entityType, world
) {
    override fun isCustomNameVisible(): Boolean {
        return true
    }

    val cpuUsage: Double = 10.0
    val cpuCapacity: Double = 100.0


}