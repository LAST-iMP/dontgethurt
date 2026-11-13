package com.lastimp.dgh.mixin.entity;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @Inject(method = "hurtTo", at = @At("HEAD"), cancellable = true)
    private void dgh$applyHealthWithoutHurtAnimation(float health, CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (!HealthCapability.has(player)) {
            return;
        }

        player.setHealth(health);
        ci.cancel();
    }
}
