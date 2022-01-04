package dev.strrl.chaoscraft.mod.entity

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


        val anotherText = Text.of("I am going to sleep").copy()
        anotherText.style = Style.EMPTY.withColor(0x4299f5).withBold(true)
        matrixStack.push()
        matrixStack.translate(0.0, 0.25, 0.0)
        renderLabelIfPresent(entity, anotherText, matrixStack, vertexConsumerProvider, light)
        matrixStack.pop()

        // 0.1
        val usagePercentage = entity.cpuUsage / entity.cpuCapacity
        val text = Text.of("CPU: [|||||      ] 30%").copy()
        text.style = Style.EMPTY.withColor(0xf55a42).withBold(true)
        matrixStack.push()
        matrixStack.translate(0.0, 0.5, 0.0)
        renderLabelIfPresent(entity, text, matrixStack, vertexConsumerProvider, light)
        matrixStack.pop()

    }
}