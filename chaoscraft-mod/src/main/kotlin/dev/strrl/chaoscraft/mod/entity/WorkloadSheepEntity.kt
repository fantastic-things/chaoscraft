package dev.strrl.chaoscraft.mod.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.text.Text
import net.minecraft.world.World

class WorkloadSheepEntity(entityType: EntityType<out SheepEntity>?, world: World?) : SheepEntity(
    entityType, world
) {
    override fun isCustomNameVisible(): Boolean {
        return true
    }

    override fun hasCustomName(): Boolean {
        return true
    }

    override fun getCustomName(): Text? {
        return Text.of("i am \r\n </br>bad sheep \r\n")
    }
}