package dev.strrl.chaoscraft.mod.entity

import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModelLayers
import net.minecraft.client.render.entity.model.SheepEntityModel
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
}