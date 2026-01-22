package com.lastimp.dgh.mixin;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "isImmobile", at = @At("RETURN"), cancellable = true)
    private void isControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (livingEntity instanceof Player && Config.player_down_moving) return;
        cir.setReturnValue(cir.getReturnValue() || HealthCapability.isDying(livingEntity));
    }

    @Inject(at = @At("HEAD"), method = "handleEntityEvent", cancellable = true)
    private void handleEntityEvent(byte id, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (id == 14) Utils.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER, livingEntity);
        ci.cancel();
    }

    @Inject(method = "canBeSeenAsEnemy", at = @At("RETURN"), cancellable = true)
    public void canBeSeenAsEnemy(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        boolean attackable = !HealthCapability.has(livingEntity) || !HealthCapability.isDying(livingEntity) || HealthLivingEntityList.canBeSeenWhenLying(livingEntity.getType());
        cir.setReturnValue(cir.getReturnValue() && attackable);
    }
}
