package com.lastimp.dgh.compact.TaZC.mixin;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.tacz.guns.item.ModernKineticGunScriptAPI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModernKineticGunScriptAPI.class)
public class ModernKineticGunScriptAPIMixin {
    @Shadow(remap = false)
    private LivingEntity shooter;

    @Inject(method = "getReloadTime", at = @At("RETURN"), cancellable = true, remap = false)
    public void getReloadTime(CallbackInfoReturnable<Long> cir) {
        if (shooter instanceof Player player) {
            HealthCapability.getAndApply(player, health -> {
                int armBreak = health.armBreak();
                long newResult = cir.getReturnValue();
                if (armBreak > 0) {
                    newResult = (long) (newResult * (1 - 0.4 * armBreak));
                }
                cir.setReturnValue(newResult);
            });
        }
    }

    @Inject(method = "getBoltTime", at = @At("RETURN"), cancellable = true, remap = false)
    public void getBoltTime(CallbackInfoReturnable<Long> cir) {
        if (shooter instanceof Player player) {
            HealthCapability.getAndApply(player, health -> {
                int armBreak = health.armBreak();
                long newResult = cir.getReturnValue();
                if (armBreak > 0) {
                    newResult = (long) (newResult * (1 - 0.4 * armBreak));
                }
                cir.setReturnValue(newResult);
            });
        }
    }
}
