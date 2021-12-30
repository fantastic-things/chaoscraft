package dev.strrl.chaoscraftmod.client

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import kotlin.math.sin


class DemoBlockEntityRenderer(private val context: BlockEntityRendererFactory.Context) :
    BlockEntityRenderer<DemoBlockEntity?> {
    private val stack = ItemStack(Items.JUKEBOX, 1)
    private val seed = 1

    override fun render(
        blockEntity: DemoBlockEntity?,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()
//
//        MinecraftClient.getInstance().itemRenderer.renderItem(
//            stack,
//            ModelTransformation.Mode.GROUND,
//            light,
//            overlay,
//            matrices,
//            vertexConsumers,
//            seed,
//        )
// Calculate the current offset in the y value
        // Calculate the current offset in the y value
        val offset = sin((blockEntity!!.world!!.time + tickDelta) / 8.0) / 4.0
        // Move the item
        // Move the item
        matrices.translate(0.5, 1.25 + offset, 0.5)

        // Rotate the item
//        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity!!.world!!.time + tickDelta) * 4))

        val lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity!!.world, blockEntity.pos.up())
        MinecraftClient.getInstance().itemRenderer.renderItem(
            stack,
            ModelTransformation.Mode.GROUND,
            lightAbove,
            OverlayTexture.DEFAULT_UV,
            matrices,
            vertexConsumers,
            seed,
        )
        matrices.push()
        matrices.scale(0.5f, 0.5f, 0.5f)
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90f))
//        MinecraftClient.getInstance().textRenderer.draw(matrices, "hello", 0.0f, 0.0f, 1)
        val draw = MinecraftClient.getInstance().textRenderer.draw(
            "test",
            1f,
            1f,
            1,
            false,
            matrices.peek().model,
            vertexConsumers,
            true,
            1,
            lightAbove,
        )
        matrices.pop()
        matrices.pop()
    }
}
