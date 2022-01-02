package dev.strrl.chaoscraft.mod.block

import dev.strrl.chaoscraft.mod.GARDEN_BEACON_BLOCK_ENTITY
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

private const val KUBECONFIG_TAG = "kubeconfig"

class GardenBeaconBlockEntity(
    pos: BlockPos?, state: BlockState?
) : BlockEntity(GARDEN_BEACON_BLOCK_ENTITY, pos, state), BlockEntityClientSerializable {

    private var kubeconfig: String = ""

    override fun writeNbt(nbt: NbtCompound?): NbtCompound {
        nbt?.putString(KUBECONFIG_TAG, kubeconfig)
        return super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        kubeconfig = nbt?.getString(KUBECONFIG_TAG)!!
    }


    override fun toClientTag(tag: NbtCompound): NbtCompound {
        tag.putString(KUBECONFIG_TAG, kubeconfig)
        return tag
    }

    override fun fromClientTag(tag: NbtCompound?) {
        kubeconfig = tag?.getString(KUBECONFIG_TAG) ?: ""
    }

}