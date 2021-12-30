package dev.strrl.chaoscraftmod.client

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos


class DemoBlockEntity(pos: BlockPos?, state: BlockState?) :
    BlockEntity(DEMO_BLOCK_ENTITY, pos, state) {
    // Store the current value of the number
    private var number = 7

    // Serialize the BlockEntity
    override fun writeNbt(tag: NbtCompound): NbtCompound {
        super.writeNbt(tag)

        // Save the current value of the number to the tag
        tag.putInt("number", number)
        return tag
    }
    override fun readNbt(tag: NbtCompound) {
        super.readNbt(tag)
        number = tag.getInt("number")
    }
}