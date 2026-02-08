package com.lastimp.dgh.fabric.mixin;

import com.lastimp.dgh.fabric.event.callback.PlayerEventCallBack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void dgh$getDestroySpeed(BlockState blockState, CallbackInfoReturnable<Float> cir) {
        Player self = (Player)(Object)this;
        float amp = PlayerEventCallBack.BreakSpeed.EVENT.invoker().interact(self);
        cir.setReturnValue(cir.getReturnValueF() * amp);
    }
}
