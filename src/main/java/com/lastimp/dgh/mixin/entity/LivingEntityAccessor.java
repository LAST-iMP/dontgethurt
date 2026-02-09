package com.lastimp.dgh.mixin.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Invoker("doHurtEquipment")
    void dgh$doHurtEquipment(DamageSource damageSource, float damageAmount, EquipmentSlot... slots);
}
