package dev.strrl.chaoscraft.mod.entity

import net.minecraft.client.model.*
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.EnderDragonEntityRenderer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
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
        private val CRYSTAL_BEAM_LAYER: RenderLayer =
            RenderLayer.getEntitySmoothCutout(EnderDragonEntityRenderer.CRYSTAL_BEAM_TEXTURE)
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

        fun renderCrystalBeam(
            dx: Float,
            dy: Float,
            dz: Float,
            tickDelta: Float,
            age: Int,
            matrices: MatrixStack,
            vertexConsumers: VertexConsumerProvider,
            light: Int
        ) {
            val f = MathHelper.sqrt(dx * dx + dz * dz)
            val g = MathHelper.sqrt(dx * dx + dy * dy + dz * dz)
            matrices.push()
            matrices.translate(0.0, 2.0, 0.0)
            matrices.multiply(
                Vec3f.POSITIVE_Y.getRadialQuaternion(
                    (-Math.atan2(
                        dz.toDouble(),
                        dx.toDouble()
                    )).toFloat() - 1.5707964f
                )
            )
            matrices.multiply(
                Vec3f.POSITIVE_X.getRadialQuaternion(
                    (-Math.atan2(
                        f.toDouble(),
                        dy.toDouble()
                    )).toFloat() - 1.5707964f
                )
            )
            val vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER)
            val h = 0.0f - (age.toFloat() + tickDelta) * 0.01f
            val i = MathHelper.sqrt(dx * dx + dy * dy + dz * dz) / 32.0f - (age.toFloat() + tickDelta) * 0.01f
            // val j: Int = 0
            var k = 0.0f
            var l = 0.75f
            var m = 0.0f
            val entry = matrices.peek()
            val matrix4f = entry.model
            val matrix3f = entry.normal
            for (n in 1..8) {
                val o = MathHelper.sin(n.toFloat() * 6.2831855f / 8.0f) * 0.75f
                val p = MathHelper.cos(n.toFloat() * 6.2831855f / 8.0f) * 0.75f
                val q = n.toFloat() / 8.0f
                vertexConsumer.vertex(matrix4f, k * 0.2f, l * 0.2f, 0.0f).color(0, 0, 0, 255).texture(m, h)
                    .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next()
                vertexConsumer.vertex(matrix4f, k, l, g).color(255, 255, 255, 255).texture(m, i)
                    .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next()
                vertexConsumer.vertex(matrix4f, o, p, g).color(255, 255, 255, 255).texture(q, i)
                    .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next()
                vertexConsumer.vertex(matrix4f, o * 0.2f, p * 0.2f, 0.0f).color(0, 0, 0, 255).texture(q, h)
                    .overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0f, -1.0f, 0.0f).next()
                k = o
                l = p
                m = q
            }
            matrices.pop()
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
        val blockPos = BlockPos(networkCrystalEntity.x + 5, networkCrystalEntity.y + 5, networkCrystalEntity.z + 5)
        if (blockPos != null) {
            val m = blockPos.x.toFloat() + 0.5f
            val n = blockPos.y.toFloat() + 0.5f
            val o = blockPos.z.toFloat() + 0.5f
            val p = (m.toDouble() - networkCrystalEntity.x).toFloat()
            val q = (n.toDouble() - networkCrystalEntity.y).toFloat()
            val r = (o.toDouble() - networkCrystalEntity.z).toFloat()
            matrixStack.translate(p.toDouble(), q.toDouble(), r.toDouble())
            val lightAbove = WorldRenderer.getLightmapCoordinates(
                networkCrystalEntity!!.world,
                BlockPos(networkCrystalEntity.pos).up()
            )
            renderCrystalBeam(
                -p,
                -q + h,
                -r,
                g,
                networkCrystalEntity.endCrystalAge,
                matrixStack,
                vertexConsumerProvider,
                lightAbove
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