package dev.strrl.chaoscraft.mod.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.world.World

class WorkloadSheepEntity(entityType: EntityType<out SheepEntity>?, world: World?) : SheepEntity(
    entityType, world
) {}