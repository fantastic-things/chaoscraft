package dev.strrl.chaoscraft.mod.entity

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.EntityModelLoader
import net.minecraft.client.render.entity.model.SheepEntityModel
import net.minecraft.client.render.entity.model.SheepWoolEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.passive.SheepEntity
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier

@Environment(value = EnvType.CLIENT)
class WorkloadSheepWoolFeatureRenderer(
    context: FeatureRendererContext<WorkloadSheepEntity, SheepEntityModel<WorkloadSheepEntity>>,
    loader: EntityModelLoader
) : FeatureRenderer<WorkloadSheepEntity, SheepEntityModel<WorkloadSheepEntity>>(context) {
    private val model: SheepWoolEntityModel<WorkloadSheepEntity>

    init {
        model = SheepWoolEntityModel(loader.getModelPart(EntityModelLayers.SHEEP_FUR))
    }

    override fun render(
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int,
        sheepEntity: WorkloadSheepEntity,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        val vertexConsumer: Float
        val bl: Float
        val minecraftClient: Float
        if (sheepEntity.isSheared) {
            return
        }
        if (sheepEntity.isInvisible) {
            val minecraftClient2 = MinecraftClient.getInstance()
            val bl2 = minecraftClient2.hasOutline(sheepEntity)
            if (bl2) {
                this.contextModel!!.copyStateTo(model)
                model.animateModel(sheepEntity, f, g, h)
                model.setAngles(sheepEntity, f, g, j, k, l)
                val vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getOutline(SKIN))
                model.render(
                    matrixStack,
                    vertexConsumer2,
                    i,
                    LivingEntityRenderer.getOverlay(sheepEntity, 0.0f),
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f
                )
            }
            return
        }
        if (sheepEntity.hasCustomName() && "jeb_" == sheepEntity.name.asString()) {
            val m = 25
            val n = sheepEntity.age / 25 + sheepEntity.id
            val o = DyeColor.values().size
            val p = n % o
            val q = (n + 1) % o
            val r = ((sheepEntity.age % 25).toFloat() + h) / 25.0f
            val fs = SheepEntity.getRgbColor(DyeColor.byId(p))
            val gs = SheepEntity.getRgbColor(DyeColor.byId(q))
            minecraftClient = fs[0] * (1.0f - r) + gs[0] * r
            bl = fs[1] * (1.0f - r) + gs[1] * r
            vertexConsumer = fs[2] * (1.0f - r) + gs[2] * r
        } else {
            val m = SheepEntity.getRgbColor(sheepEntity.color)
            minecraftClient = m[0]
            bl = m[1]
            vertexConsumer = m[2]
        }
        render(
            this.contextModel,
            model,
            SKIN,
            matrixStack,
            vertexConsumerProvider,
            i,
            sheepEntity,
            f,
            g,
            j,
            k,
            l,
            h,
            minecraftClient,
            bl,
            vertexConsumer
        )
    }

    companion object {
        private val SKIN = Identifier("textures/entity/sheep/sheep_fur.png")
    }
}

