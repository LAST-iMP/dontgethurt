package com.lastimp.dgh.source.client.render.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class OperatingBedSpecialRenderer implements NoDataSpecialModelRenderer {
    private final OperatingBedRenderer bedRenderer;
    private final Material material;

    public OperatingBedSpecialRenderer(OperatingBedRenderer bedRenderer, Material material) {
        this.bedRenderer = bedRenderer;
        this.material = material;
    }

    @Override
    public void submit(
            ItemDisplayContext context, PoseStack poseStack, SubmitNodeCollector collector, int p_439951_, int p_439919_, boolean p_440058_, int p_451700_
    ) {
        this.bedRenderer.submitSpecial(poseStack, collector, p_439951_, p_439919_, this.material, p_451700_);
    }

    @Override
    public void getExtents(Consumer<Vector3fc> p_470701_) {
        this.bedRenderer.getExtents(p_470701_);
    }

    public record Unbaked(Identifier texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<OperatingBedSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                unbaked -> unbaked.group(Identifier.CODEC.fieldOf("texture").forGetter(OperatingBedSpecialRenderer.Unbaked::texture))
                        .apply(unbaked, OperatingBedSpecialRenderer.Unbaked::new)
        );

        public Unbaked() {
            this(OperatingBedRenderer.texture);
        }

        @Override
        public MapCodec<OperatingBedSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(SpecialModelRenderer.BakingContext p_435542_) {
            return new OperatingBedSpecialRenderer(new OperatingBedRenderer(p_435542_), OperatingBedRenderer.material);
        }
    }
}
