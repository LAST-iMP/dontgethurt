package com.lastimp.dgh.mixin.entity;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryEventHandler;
import com.lastimp.dgh.common.config.impl.HealthLivingEntityList;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        if (livingEntity instanceof Player && PlatformService.CONFIG.PLAYER_DOWN_MOVING()) return;
        cir.setReturnValue(cir.getReturnValue() || HealthCapability.isDown(livingEntity));
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

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At("HEAD"), cancellable = true)
    protected void getDamageAfterArmorAbsorb(DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Float> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (InjuryEventHandler.canInjuryBody(livingEntity, damageSource)) {
            cir.setReturnValue(damageAmount);
        }
    }

    @Inject(method = "onEquipItem", at = @At("TAIL"))
    public void onEquipItem(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object)this;
        if (!HealthCapability.has(livingEntity)) return;
        HealthCapability.getAndApply(livingEntity, HealthCapability::refreshArmor);
    }
}
