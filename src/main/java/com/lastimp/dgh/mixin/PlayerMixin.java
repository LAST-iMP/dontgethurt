package com.lastimp.dgh.mixin;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "canEat", at = @At("RETURN"), cancellable = true)
    public void canEat(boolean canAlwaysEat, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        HealthCapability.getAndApply(player, h -> cir.setReturnValue(cir.getReturnValue() && h.canEat()));
    }
}
