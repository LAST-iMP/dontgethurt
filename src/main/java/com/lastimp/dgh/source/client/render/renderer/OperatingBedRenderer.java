package com.lastimp.dgh.source.client.render.renderer;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.client.render.MyModelLayers;
import com.lastimp.dgh.source.client.render.renderState.OperatingBedRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.client.renderer.Sheets.BED_SHEET;

public class OperatingBedRenderer implements BlockEntityRenderer<OperatingBedBlock.@NotNull Entity, @NotNull OperatingBedRenderState> {
    public static final Identifier texture = Common.getId(DontGetHurt.MODID, "entity/bed/operating_bed");
    public static final Material material = new Material(BED_SHEET, texture);
    private final MaterialSet materials;
    private final Model.Simple headRoot;
    private final Model.Simple footRoot;

    public OperatingBedRenderer(BlockEntityRendererProvider.Context context) {
        this(context.materials(), context.entityModelSet());
    }

    public OperatingBedRenderer(SpecialModelRenderer.BakingContext context) {
        this(context.materials(), context.entityModelSet());
    }

    public OperatingBedRenderer(MaterialSet materials, EntityModelSet modelSet) {
        this.materials = materials;
        this.headRoot = new Model.Simple(modelSet.bakeLayer(MyModelLayers.OPERATING_BED_HEAD), RenderTypes::entitySolid);
        this.footRoot = new Model.Simple(modelSet.bakeLayer(MyModelLayers.OPERATING_BED_FOOT), RenderTypes::entitySolid);
    }

    @Override
    public OperatingBedRenderState createRenderState() {
        return new OperatingBedRenderState();
    }

    @Override
    public void extractRenderState(OperatingBedBlock.Entity blockEntity, @NotNull OperatingBedRenderState renderState, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.facing = blockEntity.getBlockState().getValue(BedBlock.FACING);
        renderState.isHead = blockEntity.getBlockState().getValue(BedBlock.PART) == BedPart.HEAD;
        if (blockEntity.getLevel() != null) {
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborcombineresult = DoubleBlockCombiner.combineWithNeigbour(
                    BlockEntityType.BED,
                    BedBlock::getBlockType,
                    BedBlock::getConnectedDirection,
                    ChestBlock.FACING,
                    blockEntity.getBlockState(),
                    blockEntity.getLevel(),
                    blockEntity.getBlockPos(),
                    (p_112202_, p_112203_) -> false
            );
            renderState.lightCoords = neighborcombineresult.apply(new BrightnessCombiner<>()).get(renderState.lightCoords);
        }
    }

    @Override
    public void submit(OperatingBedRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        this.submitPiece(
                poseStack,
                nodeCollector,
                renderState.isHead ? this.headRoot : this.footRoot,
                renderState.facing,
                material,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0
        );
    }

    public void submitSpecial(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, Material material, int outlineColor) {
        this.submitPiece(poseStack, nodeCollector, this.headRoot, Direction.SOUTH, material, packedLight, packedOverlay, outlineColor);
        this.submitPiece(poseStack, nodeCollector, this.footRoot, Direction.SOUTH, material, packedLight, packedOverlay, outlineColor);
    }

    private void submitPiece(
            PoseStack poseStack,
            SubmitNodeCollector nodeCollector,
            Model.Simple model,
            Direction direction,
            Material material,
            int packedLight,
            int packedOverlay,
            int outlineColor
    ) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(-0.5F, -1.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + direction.toYRot()));
        nodeCollector.submitModel(
                model,
                Unit.INSTANCE,
                poseStack,
                material.renderType(RenderTypes::entitySolid),
                packedLight,
                packedOverlay,
                -1,
                this.materials.get(material),
                outlineColor,
                null
        );
        poseStack.popPose();
    }

    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + Direction.SOUTH.toYRot()));
        this.headRoot.root().getExtentsForGui(poseStack, output);
        poseStack.setIdentity();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + Direction.NORTH.toYRot()));
        this.footRoot.root().getExtentsForGui(poseStack, output);
    }

    public static LayerDefinition createHeadLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create()
                .texOffs(0, 20).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 4.0F, 16.0F)
                .texOffs(0, 50).addBox(-3.0F, -2.0F, -8.0F, 6.0F, 2.0F, 8.0F)
                .texOffs(16, 12).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 3.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r1 = group.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 43).addBox(-8.0F, -2.0F, 0.0F, 8.0F, 2.0F, 5.0F), PartPose.offsetAndRotation(4.0F, -7.0F, 3.0F, 0.3927F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createFootLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 4.0F, 16.0F)
                .texOffs(0, 56).addBox(6.0F, -11.0F, -5.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(0, 54).addBox(6.0F, -12.0F, -8.0F, 2.0F, 1.0F, 9.0F)
                .texOffs(0, 56).addBox(-8.0F, -11.0F, -5.0F, 2.0F, 2.0F, 2.0F)
                .texOffs(0, 54).addBox(-8.0F, -12.0F, -8.0F, 2.0F, 1.0F, 9.0F)
                .texOffs(0, 50).addBox(-3.0F, -2.0F, -8.0F, 6.0F, 2.0F, 8.0F)
                .texOffs(16, 12).addBox(-2.0F, -5.0F, -8.0F, 4.0F, 3.0F, 4.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
