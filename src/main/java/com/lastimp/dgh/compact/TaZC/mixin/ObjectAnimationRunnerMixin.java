package com.lastimp.dgh.compact.TaZC.mixin;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.tacz.guns.api.client.animation.ObjectAnimationRunner;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.entity.ReloadState;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ObjectAnimationRunner.class, remap = false)
public class ObjectAnimationRunnerMixin {
    private @Unique long dgh$lastTimeUnscaled;
    private @Unique long dgh$lastTimeScaled;

    static {
        MixinExtrasBootstrap.init();
    }

    {
        dgh$lastTimeScaled = dgh$lastTimeUnscaled = System.nanoTime();
    }

    @ModifyExpressionValue(
            method = {"update", "updateSoundOnly"},
            at = @At(value = "INVOKE", target = "Ljava/lang/System;nanoTime()J"))
    private long timeScaler(long original) {
        var player = Minecraft.getInstance().player;
        var reloadState = IGunOperator.fromLivingEntity(player).getSynReloadState();
        boolean check = reloadState.getStateType() != ReloadState.StateType.NOT_RELOADING;
        check |= IGunOperator.fromLivingEntity(player).getSynIsBolting();

        var health = HealthCapability.get(player);
        var scale = check ? 1.0 - 0.4 * health.armBreak() : 1;

        var deltaUnscaled = original - dgh$lastTimeUnscaled;
        dgh$lastTimeUnscaled += deltaUnscaled;
        dgh$lastTimeScaled += (long) (deltaUnscaled * scale);
        return dgh$lastTimeScaled;
    }
}
