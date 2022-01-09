package dev.strrl.chaoscraft.mod.enchantment

import dev.strrl.chaoscraft.mod.entity.NetworkCrystalEntity
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentTarget
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items

class NetworkBlackHoleEnchantment : Enchantment(
    Rarity.UNCOMMON,
    EnchantmentTarget.WEAPON,
    arrayOf(
        EquipmentSlot.MAINHAND,
        EquipmentSlot.OFFHAND
    )
) {
    override fun onTargetDamaged(user: LivingEntity, target: Entity, level: Int) {
        if (user is PlayerEntity && target is NetworkCrystalEntity) {
            if (user.offHandStack.item == Items.DIAMOND_SWORD) {
                target.updateNetworkDisabled(!target.fetchNetworkDisabled())
            }
        }
    }
}