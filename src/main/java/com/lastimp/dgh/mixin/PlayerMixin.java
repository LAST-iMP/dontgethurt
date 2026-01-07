package com.lastimp.dgh.mixin;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(at = @At("HEAD"), method = "updatePlayerPose", cancellable = true)
    private void incapacitatedUpdatePlayerPose(CallbackInfo ci){
        Player player = (Player)(Object)this;
        if (HealthCapability.has(player) && HealthCapability.isDying(player)) {
            if (player.getVehicle() != null) return;
            ci.cancel();
        }
    }
}

