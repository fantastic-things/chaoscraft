package dev.strrl.chaoscraft.mod.show

class MemoryRender(
    private val resourceName: String,
    private val memoryPercentage: Double,
) {
    private val length = 20
    fun render(): String {
        val usedLength = (length * memoryPercentage).toInt()
        val unusedLength = length - usedLength
        val used = "#".repeat(usedLength)
        val unused = " ".repeat(unusedLength)
        val percentage = (memoryPercentage * 100).toInt()
        return "$resourceName:[$used$unused] $percentage%"
    }

}