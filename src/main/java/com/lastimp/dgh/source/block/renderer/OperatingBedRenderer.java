package com.lastimp.dgh.source.block.renderer;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.register.MyModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import static net.minecraft.client.renderer.Sheets.BED_SHEET;

public class OperatingBedRenderer implements BlockEntityRenderer<OperatingBedBlock.Entity> {
    private static final Material material = new Material(BED_SHEET, Common.ResourceLocation(DontGetHurt.MODID, "entity/bed/operating_bed"));
    private final ModelPart headRoot;
    private final ModelPart footRoot;

    public OperatingBedRenderer(BlockEntityRendererProvider.Context context) {
        this.headRoot = context.bakeLayer(MyModelLayers.OPERATING_BED_HEAD);
        this.footRoot = context.bakeLayer(MyModelLayers.OPERATING_BED_FOOT);
    }

    @Override
    public void render(OperatingBedBlock.Entity entity, float v, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Level level = entity.getLevel();
        if (level != null) {
            BlockState blockstate = entity.getBlockState();
            DoubleBlockCombiner.NeighborCombineResult<? extends BedBlockEntity> neighborcombineresult = DoubleBlockCombiner.combineWithNeigbour(BlockEntityType.BED, BedBlock::getBlockType, BedBlock::getConnectedDirection, ChestBlock.FACING, blockstate, level, entity.getBlockPos(), (p_112202_, p_112203_) -> false);
            int i = ((Int2IntFunction)neighborcombineresult.apply(new BrightnessCombiner())).get(packedLight);
            this.renderPiece(poseStack, buffer, blockstate.getValue(BedBlock.PART) == BedPart.HEAD ? this.headRoot : this.footRoot, blockstate.getValue(BedBlock.FACING), material, i, packedOverlay, false);
        } else {
            this.renderPiece(poseStack, buffer, this.headRoot, Direction.SOUTH, material, packedLight, packedOverlay, false);
            this.renderPiece(poseStack, buffer, this.footRoot, Direction.SOUTH, material, packedLight, packedOverlay, true);
        }
    }

    private void renderPiece(PoseStack poseStack, MultiBufferSource bufferSource, ModelPart modelPart, Direction direction, Material material, int packedLight, int packedOverlay, boolean foot) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        poseStack.translate(-0.5F, -1.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F + direction.toYRot()));
        VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entitySolid);
        modelPart.render(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
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
