package dev.strrl.chaoscraft.mod.entity

import net.minecraft.client.model.*
import net.minecraft.client.render.Frustum
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EnderDragonEntityRenderer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import kotlin.math.sin

class NetworkCrystalEntityRenderer(
    context: EntityRendererFactory.Context,
) : EntityRenderer<EndCrystalEntity>(context) {
    private var core: ModelPart? = null
    private var frame: ModelPart? = null
    private var bottom: ModelPart? = null

    companion object {
        val TEXTURE = Identifier("textures/entity/end_crystal/end_crystal.png")
        val END_CRYSTAL: RenderLayer = RenderLayer.getEntityCutoutNoCull(TEXTURE)
        val SINE_45_DEGREES = sin(0.7853981633974483).toFloat()
        fun getTexturedModelData(): TexturedModelData? {
            val modelData = ModelData()
            val modelPartData = modelData.root
            modelPartData.addChild(
                "glass",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),
                ModelTransform.NONE
            )
            modelPartData.addChild(
                "cube",
                ModelPartBuilder.create().uv(32, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),
                ModelTransform.NONE
            )
            modelPartData.addChild(
                "base",
                ModelPartBuilder.create().uv(0, 16).cuboid(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f),
                ModelTransform.NONE
            )
            return TexturedModelData.of(modelData, 64, 32)
        }
    }

    init {
        shadowRadius = 0.5f
        val modelPart = context.getPart(EntityModelLayers.END_CRYSTAL)
        this.core = modelPart.getChild("cube")
        this.frame = modelPart.getChild("glass")
        this.bottom = modelPart.getChild("base")
    }

    override fun getTexture(networkCrystalEntity: EndCrystalEntity?): Identifier {
        return TEXTURE
    }

    override fun shouldRender(
        networkCrystalEntity: EndCrystalEntity,
        frustum: Frustum?,
        d: Double,
        e: Double,
        f: Double
    ): Boolean {
        return super.shouldRender(networkCrystalEntity, frustum, d, e, f) || networkCrystalEntity.beamTarget != null
    }

    override fun render(
        networkCrystalEntity: EndCrystalEntity,
        f: Float,
        g: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int
    ) {
        matrixStack.push()
        val h = getYOffset(networkCrystalEntity, g)
        val j = (networkCrystalEntity.endCrystalAge.toFloat() + g) * 3.0f
        val vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL)
        matrixStack.push()
        matrixStack.scale(2.0f, 2.0f, 2.0f)
        matrixStack.translate(0.0, -0.5, 0.0)
        val k = OverlayTexture.DEFAULT_UV
        if (networkCrystalEntity.shouldShowBottom()) {
            bottom!!.render(matrixStack, vertexConsumer, i, k)
        }
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j))
        matrixStack.translate(0.0, (1.5f + h / 2.0f).toDouble(), 0.0)
        matrixStack.multiply(
            Quaternion(
                Vec3f(
                    SINE_45_DEGREES,
                    0.0f,
                    SINE_45_DEGREES
                ), 60.0f, true
            )
        )
        frame!!.render(matrixStack, vertexConsumer, i, k)
        val l = 0.875f
        matrixStack.scale(0.875f, 0.875f, 0.875f)
        matrixStack.multiply(
            Quaternion(
                Vec3f(
                    SINE_45_DEGREES,
                    0.0f,
                    SINE_45_DEGREES
                ), 60.0f, true
            )
        )
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j))
        frame!!.render(matrixStack, vertexConsumer, i, k)
        matrixStack.scale(0.875f, 0.875f, 0.875f)
        matrixStack.multiply(
            Quaternion(
                Vec3f(
                    SINE_45_DEGREES,
                    0.0f,
                    SINE_45_DEGREES
                ), 60.0f, true
            )
        )
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(j))
        core!!.render(matrixStack, vertexConsumer, i, k)
        matrixStack.pop()
        matrixStack.pop()
        val blockPos = networkCrystalEntity.beamTarget
        if (blockPos != null) {
            val m = blockPos.x.toFloat() + 0.5f
            val n = blockPos.y.toFloat() + 0.5f
            val o = blockPos.z.toFloat() + 0.5f
            val p = (m.toDouble() - networkCrystalEntity.x).toFloat()
            val q = (n.toDouble() - networkCrystalEntity.y).toFloat()
            val r = (o.toDouble() - networkCrystalEntity.z).toFloat()
            matrixStack.translate(p.toDouble(), q.toDouble(), r.toDouble())
            EnderDragonEntityRenderer.renderCrystalBeam(
                -p,
                -q + h,
                -r,
                g,
                networkCrystalEntity.endCrystalAge,
                matrixStack,
                vertexConsumerProvider,
                i
            )
        }
        super.render(networkCrystalEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }

    private fun getYOffset(crystal: EndCrystalEntity, tickDelta: Float): Float {
        val f = crystal.endCrystalAge.toFloat() + tickDelta
        var g = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f
        g = (g * g + g) * 0.4f
        return g - 1.4f
    }
}