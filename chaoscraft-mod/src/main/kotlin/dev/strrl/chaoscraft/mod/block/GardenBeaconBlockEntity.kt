package dev.strrl.chaoscraft.mod.block

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.strrl.chaoscraft.api.Workload
import dev.strrl.chaoscraft.mod.entity.ChaoscraftEntityType
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import java.util.*

private const val KUBECONFIG_TAG = "kubeconfig"
private const val STATE = "state"

private val mapper: ObjectMapper = jacksonObjectMapper()

class GardenBeaconBlockEntity(
    pos: BlockPos?, state: BlockState?
) : BlockEntity(ChaoscraftEntityType.GARDEN_BEACON_BLOCK_ENTITY, pos, state), BlockEntityClientSerializable {

    var state: State = emptyState()
        set(value) {
            field = value
            this.markDirty()
        }

    var kubeconfig: String = ""
        set(value) {
            field = value
            this.markDirty()
        }

    override fun writeNbt(nbt: NbtCompound?): NbtCompound {
        nbt?.putString(KUBECONFIG_TAG, kubeconfig)
        nbt?.putString(STATE, mapper.writeValueAsString(state))
        return super.writeNbt(nbt)
    }

    override fun readNbt(nbt: NbtCompound?) {
        super.readNbt(nbt)
        kubeconfig = nbt?.getString(KUBECONFIG_TAG) ?: ""
        state = nbt?.getString(STATE)?.let {
            mapper.readValue(it, State::class.java)
        } ?: emptyState()
    }


    override fun toClientTag(tag: NbtCompound): NbtCompound {
        tag.putString(KUBECONFIG_TAG, kubeconfig)
        tag.putString(STATE, mapper.writeValueAsString(state))
        return tag
    }

    override fun fromClientTag(tag: NbtCompound?) {
        kubeconfig = tag?.getString(KUBECONFIG_TAG) ?: ""
        state = tag?.getString(STATE)?.let {
            mapper.readValue(it, State::class.java)
        } ?: emptyState()
    }
}

data class State(
    val workloads: Set<Workload> = mutableSetOf(),
    val controlledEntityIds: Set<UUID> = mutableSetOf()
)

fun emptyState(): State {
    return State(mutableSetOf(), mutableSetOf())
}
