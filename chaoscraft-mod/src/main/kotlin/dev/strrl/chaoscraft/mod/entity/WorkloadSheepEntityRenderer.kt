package dev.strrl.chaoscraft.mod.entity

import dev.strrl.chaoscraft.mod.show.MemoryRender
import dev.strrl.chaoscraft.mod.show.UsageRender
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.SheepEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class WorkloadSheepEntityRenderer(
    context: EntityRendererFactory.Context,
) : MobEntityRenderer<WorkloadSheepEntity, SheepEntityModel<WorkloadSheepEntity>>(
    context, SheepEntityModel<WorkloadSheepEntity>(context.getPart(EntityModelLayers.SHEEP)), 0.7f
) {

    init {
        addFeature(WorkloadSheepWoolFeatureRenderer(this, context.modelLoader))
    }

    private val texture = Identifier("textures/entity/sheep/sheep.png")

    override fun getTexture(entity: WorkloadSheepEntity?): Identifier {
        return texture;
    }

    override fun render(
        entity: WorkloadSheepEntity,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider?,
        light: Int
    ) {
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, light)

        val usagePercentage = entity.cpuUsage / entity.cpuCapacity
        val usageRender = UsageRender("usage",usagePercentage)
        val usageText = usageRender.render()
        val text = Text.of(usageText).copy()
        text.style = Style.EMPTY.withColor(0xe0766e).withBold(true).withFormatting()
        matrixStack.push()
        matrixStack.translate(0.0, 0.5, 0.0)
        renderLabelIfPresent(entity, text, matrixStack, vertexConsumerProvider, light)
        matrixStack.pop()

        val memoryPercentage = entity.memoryUsage / entity.memoryCapacity
        val memoryRender = MemoryRender("memory",memoryPercentage)
        val memoryText = memoryRender.render()
        val anotherText = Text.of(memoryText).copy()
        anotherText.style = Style.EMPTY.withColor(0x6e94e0).withBold(true)
        matrixStack.push()
        matrixStack.translate(0.0, 0.25, 0.0)
        renderLabelIfPresent(entity, anotherText, matrixStack, vertexConsumerProvider, light)
        matrixStack.pop()
    }
}