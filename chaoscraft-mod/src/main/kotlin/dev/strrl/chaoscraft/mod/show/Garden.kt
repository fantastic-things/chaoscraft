package dev.strrl.chaoscraft.mod.show

import dev.strrl.chaoscraft.api.Workload
import net.minecraft.entity.Entity
import net.minecraft.world.World


/**
 * [Garden] is the base playground to show the workloads.
 */
data class Garden(
    val world: World,
    val base: Position,
    val workloads: MutableList<Workload>,
    val entities: MutableList<Entity>,
)

class Gardens {
    companion object {
        private val gardens: MutableMap<Position, Garden> = mutableMapOf()

        fun gardens(): List<Garden> {
            return gardens.values.toList()
        }

        fun registration(position: Position, world: World) {
            if (!gardens.containsKey(position)) {
                gardens[position] = Garden(world, position, mutableListOf(), mutableListOf())
            }
        }

        fun unregistration(position: Position) {
            gardens.remove(position)
        }
    }
}