package com.lastimp.dgh.mixin;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "setupRotations", at = @At("HEAD"), cancellable = true)
    private void onSetupRotations(AbstractClientPlayer player, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci) {
        if (HealthCapability.isDying(player)) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F)); // 横躺
            poseStack.translate(0, -0.7, 0); // 位置调整
        }
    }
}
