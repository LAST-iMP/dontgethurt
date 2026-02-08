package com.lastimp.dgh.fabric.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.event.IComponentDamageEvent;
import com.lastimp.dgh.common.event.IEventHook;
import com.lastimp.dgh.common.event.IHealthDamageEvent;
import com.lastimp.dgh.fabric.event.callback.ComponentDamageCallback;
import com.lastimp.dgh.fabric.event.callback.HealthDamageCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class EventHooksNF implements IEventHook {
    public IHealthDamageEvent fireDghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        var dghHealthDamageEvent = new HealthDamageEvent(source, sourceDamage, newDamage);
        return HealthDamageCallback.EVENT.invoker().interact(dghHealthDamageEvent);
    }

    public IComponentDamageEvent fireDghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        var event = new ComponentDamageEvent(blocking, component, damageAmount, resist, type);
        return ComponentDamageCallback.EVENT.invoker().interact(event);
    }

    @Override
    public boolean onLivingUseTotem(LivingEntity entity, DamageSource damageSource, ItemStack totem, InteractionHand hand) {
        return true;
    }
}
