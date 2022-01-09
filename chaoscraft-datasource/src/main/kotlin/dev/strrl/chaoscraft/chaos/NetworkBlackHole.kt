package dev.strrl.chaoscraft.chaos

val TEMPLATE: String = """
apiVersion: chaos-mesh.org/v1alpha1
kind: NetworkChaos
metadata:
  name: "network-black-hole-{NAME}"
  namespace: "{NAMESPACE}"
spec:
  action: partition
  mode: all
  selector:
    pods:
      "{NAMESPACE}":
      - "{NAME}"
  direction: both
""".trimIndent()

class NetworkBlackHole {

    companion object {

        fun inject(namespace: String, name: String) {
            val command = "kubectl -n $namespace apply -f -"
            println(command)
            val process = Runtime.getRuntime().exec(command)
            val stdinWriter = process.outputWriter()
            val stdoutReader = process.inputReader()
            val stderrReader = process.errorReader()
            stdinWriter.write(TEMPLATE.replace("{NAME}", name).replace("{NAMESPACE}", namespace))
            stdinWriter.close()
            println(stdoutReader.readText())
            println(stderrReader.readText())
        }

        fun recover(namespace: String, name: String) {
            val command = "kubectl delete networkchaos network-black-hole-$name -n $namespace"
            println(command)
            Runtime.getRuntime().exec(command)
        }
    }
}