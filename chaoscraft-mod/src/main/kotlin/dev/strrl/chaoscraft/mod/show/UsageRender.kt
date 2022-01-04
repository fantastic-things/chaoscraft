package dev.strrl.chaoscraft.mod.show

class UsageRender(
    private val resourceName: String,
    private val usagePercentage: Double,
) {
    private val length = 20
    fun render(): String {
        val usedLength = (length * usagePercentage).toInt()
        val unusedLength = length - usedLength
        val used = "#".repeat(usedLength)
        val unused = " ".repeat(unusedLength)
        val percentage = (usagePercentage * 100).toInt()
        return "$resourceName:[$used$unused] $percentage%"
    }
}