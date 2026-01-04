package com.lastimp.dgh.mixin;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class EntityMixin {
    @Inject(method = "isImmobile", at = @At("RETURN"), cancellable = true)
    private void isControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        cir.setReturnValue(livingEntity.isDeadOrDying() || HealthCapability.isDying(livingEntity));
    }
}
