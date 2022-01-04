package dev.strrl.chaoscraft.mod.show

class MemoryRender(
    private val resourceName: String,
    private val memoryPercentage: Double,
) {
    private val length = 20
    fun render(): String {
        if (memoryPercentage > 1.0) {
            val used = "#".repeat(this.length)
            val percentage = (memoryPercentage * 100).toInt()
            return "$resourceName:[$used] $percentage%"
        }
        if (memoryPercentage < 0.0) {
            val unused = " ".repeat(this.length)
            return "$resourceName:[$unused] 0%"
        }
        val usedLength = (length * memoryPercentage).toInt()
        val unusedLength = length - usedLength
        val used = "#".repeat(usedLength)
        val unused = " ".repeat(unusedLength)
        val percentage = (memoryPercentage * 100).toInt()
        return "$resourceName:[$used$unused] $percentage%"
    }

}