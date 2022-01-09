package dev.strrl.chaoscraft.chaos

class PodKill {
    companion object {
        fun killPod(namespace: String, name: String) {
            val command = "kubectl delete pod $name -n $namespace --force --grace-period 0"
            println(command)
            Runtime.getRuntime().exec(command)
        }
    }
}