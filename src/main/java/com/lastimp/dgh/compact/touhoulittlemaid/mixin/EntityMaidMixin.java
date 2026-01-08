package com.lastimp.dgh.compact.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityMaid.class)
public class EntityMaidMixin {
    @Inject(method = "randomRestoreHealth", at = @At("HEAD"), cancellable = true, remap = false)
    private void randomRestoreHealth(CallbackInfo ci) {
        ci.cancel();
    }
}
