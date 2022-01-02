package dev.strrl.chaoscraft.mod.show

import com.google.common.util.concurrent.ThreadFactoryBuilder
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * [Manager] is [Gardener]s' manager.
 */
class Manager {
    companion object {
        private val done: AtomicBoolean = AtomicBoolean(false)
        private val managerThreadPool: ExecutorService = ThreadPoolExecutor(
            1, 1,
            60L, TimeUnit.SECONDS,
            ArrayBlockingQueue(1),
            ThreadFactoryBuilder().setNameFormat("Gardener-Manager-%d").build()
        )
        private val workerThreadPool: ExecutorService = ThreadPoolExecutor(
            4, 4,
            60L, TimeUnit.SECONDS,
            ArrayBlockingQueue(1024),
            ThreadFactoryBuilder().setNameFormat("Gardener-%d").build()
        )


        fun start() {
            managerThreadPool.submit() {
                run()
            }
        }

        fun stop() {
            done.set(true)
        }

        private fun run() {
            while (!done.get()) {
                Thread.sleep(5000)
                for (garden in Gardens.gardens()) {
                    workerThreadPool.submit {

                        val gardener = Gardener(garden)
                        gardener.syncData()
                        gardener.prepareGarden()
                    }
                }
            }
        }
    }
}