package com.lastimp.dgh.compact.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidHealSelfTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaidHealSelfTask.class)
public class MaidHealSelfTaskMixin {
    @Inject(method = "checkExtraStartConditions(Lnet/minecraft/server/level/ServerLevel;Lcom/github/tartaricacid/touhoulittlemaid/entity/passive/EntityMaid;)Z",
            at = @At("RETURN"),
            cancellable = true, remap = false)
    private void checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
