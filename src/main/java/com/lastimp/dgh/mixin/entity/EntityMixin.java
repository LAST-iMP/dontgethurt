package com.lastimp.dgh.mixin.entity;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isEffectiveAi", at = @At("RETURN"), cancellable = true)
    private void isEffectiveAi(CallbackInfoReturnable<Boolean> cir) {
        if ((Entity) (Object)this instanceof Mob mob) {
            cir.setReturnValue(cir.getReturnValue() && !HealthCapability.isDown(mob));
        }
    }

    @Inject(method = "isControlledByLocalInstance", at = @At("RETURN"), cancellable = true)
    private void isControlledByLocalInstance(CallbackInfoReturnable<Boolean> cir) {
        if ((Entity) (Object) this instanceof Mob mob && !(mob.getControllingPassenger() instanceof Player)) {
            cir.setReturnValue(!mob.level().isClientSide() && !mob.isNoAi());
        }
    }

    @Inject(method = "getMaxAirSupply", at = @At("RETURN"), cancellable = true)
    private void getMaxAirSupply(CallbackInfoReturnable<Integer> cir) {
        Entity self = (Entity) (Object) this;
        // 使用公共方法获取世界，如果为 null 说明实体尚未初始化（可能正在构造中）
        if (self.getCommandSenderWorld() == null) {
            return;
        }
        if (self instanceof LivingEntity livingEntity) {
            try {
                HealthCapability.getAndApply(livingEntity, h -> cir.setReturnValue(h.maxAirSupply()));
            } catch (Throwable ignored) {
                // Keep vanilla return value when capability is not yet safe to query.
            }
        }
    }
}
