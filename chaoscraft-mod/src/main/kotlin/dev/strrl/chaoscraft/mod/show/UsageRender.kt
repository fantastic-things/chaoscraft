package dev.strrl.chaoscraft.mod.show

class UsageRender(
    private val resourceName: String,
    private val usagePercentage: Double,
) {
    private val length = 20
    fun render(): String {
        if (usagePercentage > 1.0) {
            val used = "#".repeat(this.length)
            val percentage = (usagePercentage * 100).toInt()
            return "$resourceName:[$used] $percentage%"
        }
        if (usagePercentage < 0.0) {
            val unused = " ".repeat(this.length)
            return "$resourceName:[$unused] 0.0%"
        }
        val usedLength = (length * usagePercentage).toInt()
        val unusedLength = length - usedLength
        val used = "#".repeat(usedLength)
        val unused = " ".repeat(unusedLength)
        val percentage = (usagePercentage * 100).toInt()
        return "$resourceName:[$used$unused] $percentage%"
    }
}