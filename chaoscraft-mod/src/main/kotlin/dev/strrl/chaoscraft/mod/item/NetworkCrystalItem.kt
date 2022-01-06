package dev.strrl.chaoscraft.mod.item

import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntity
import dev.strrl.chaoscraft.mod.entity.WorkloadSheepEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.EndCrystalItem
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import kotlin.random.Random

class NetworkCrystalItem(settings: Settings) : EndCrystalItem(settings) {
    override fun useOnEntity(stack: ItemStack?, user: PlayerEntity?, entity: LivingEntity?, hand: Hand?): ActionResult {
        if (entity is WorkloadSheepEntity) {
            val world = entity.world
            if (world is ServerWorld) {
                val networkCrystal = NetworkCrystalEntity(world, entity, Random.nextDouble(2.0, 5.0).toFloat())
                networkCrystal.setShowBottom(false)
                val succeed = world.spawnEntity(networkCrystal)
                if (!succeed) {
                    println("Spawned Network Crystal failed")
                }
            }
        }
        return super.useOnEntity(stack, user, entity, hand)
    }
}