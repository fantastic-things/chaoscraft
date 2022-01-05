package dev.strrl.chaoscraft.mod.armor

import dev.strrl.chaoscraft.mod.CHAOSCRAFT_MOD_GROUP
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorItem
import net.minecraft.item.ArmorMaterials
import net.minecraft.item.Item

class ChaoscraftArmors {
    companion object {
        val RESOURCE_USAGE_GLASS: Item =
            ArmorItem(
                ArmorMaterials.DIAMOND,
                EquipmentSlot.HEAD,
                Item.Settings().group(CHAOSCRAFT_MOD_GROUP)
            )
    }
}