package dev.strrl.chaoscraft.mod.show

class UsageRender(
    private val resourceName: String,
    private val usagePercentage: Double,
) {
    fun render(): String {

        val used = "||||"
        val unused = "     "
        return "$resourceName: [$used$unused] 10%"
    }
}