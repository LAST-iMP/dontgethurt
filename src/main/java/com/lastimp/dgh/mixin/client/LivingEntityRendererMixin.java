package com.lastimp.dgh.mixin.client;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity>{
    @Inject(method = "setupRotations", at = @At("HEAD"), cancellable = true)
    private void onSetupRotations(T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci) {
        if (HealthCapability.has(entity) && HealthCapability.isDying(entity)) {
            if (entity instanceof Player) return;
            poseStack.mulPose(Axis.YP.rotationDegrees(180F - yBodyRot));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // 横躺
            poseStack.translate(0, -0.7, 0); // 位置调整
            ci.cancel();
        }
    }
}
