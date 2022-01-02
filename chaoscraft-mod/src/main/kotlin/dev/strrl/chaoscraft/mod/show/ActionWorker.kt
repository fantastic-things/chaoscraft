package dev.strrl.chaoscraft.mod.show

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ActionWorker {
    companion object : CoroutineScope {
        private val workerPool = Executors.newWorkStealingPool().asCoroutineDispatcher()

        fun dispatch(actions: List<Action>) {
            actions.forEach {
                launch {
                    it.run()
                }
            }
        }

        override val coroutineContext: CoroutineContext
            get() = workerPool
    }

}