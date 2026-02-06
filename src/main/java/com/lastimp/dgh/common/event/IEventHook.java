package com.lastimp.dgh.common.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IEventHook {
    IHealthDamageEvent fireDghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage);

    IComponentDamageEvent fireDghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type);

    boolean onLivingUseTotem(LivingEntity entity, DamageSource damageSource, ItemStack totem, InteractionHand hand);
}
