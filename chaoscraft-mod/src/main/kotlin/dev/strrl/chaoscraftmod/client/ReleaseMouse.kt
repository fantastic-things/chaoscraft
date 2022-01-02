package dev.strrl.chaoscraftmod.client

import net.minecraft.client.MinecraftClient

fun releaseMouse() {
    try {
        MinecraftClient.getInstance().mouse.unlockCursor()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}