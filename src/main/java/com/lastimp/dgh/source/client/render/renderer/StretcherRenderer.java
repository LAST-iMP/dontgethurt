package com.lastimp.dgh.source.client.render.renderer;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.client.render.MyModelLayers;
import com.lastimp.dgh.source.client.render.entityModel.StretcherEntityModel;
import com.lastimp.dgh.source.client.render.renderState.StretcherRenderState;
import com.lastimp.dgh.source.entity.StretcherEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class StretcherRenderer extends EntityRenderer<@NotNull StretcherEntity, @NotNull StretcherRenderState> {
    private static final Identifier TEXTURE = Common.getId(DontGetHurt.MODID, "textures/entity/stretcher.png");
    private final Model.Simple patchModel;
    private final EntityModel<@NotNull StretcherRenderState> model;

    public StretcherRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayer) {
        super(context);
        this.patchModel = new Model.Simple(context.bakeLayer(MyModelLayers.STRETCHER), RenderTypes::entitySolid);
        this.model = new StretcherEntityModel(context.bakeLayer(modelLayer));
    }

    @Override
    public StretcherRenderState createRenderState() {
        return new StretcherRenderState();
    }

    @Override
    public void extractRenderState(StretcherEntity entity, StretcherRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.yRot = entity.getYRot(partialTick);
    }

    @Override
    public void submit(StretcherRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        // 水平躺放
        poseStack.translate(0, 1.5f, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.yRot));

        nodeCollector.submitModel(
                this.model, renderState, poseStack, this.model.renderType(TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, renderState.outlineColor, null
        );
        poseStack.popPose();

        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }
}
