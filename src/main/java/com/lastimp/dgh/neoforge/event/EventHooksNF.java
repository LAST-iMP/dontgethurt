package com.lastimp.dgh.neoforge.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.event.IEventHook;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;

public class EventHooksNF implements IEventHook {
    @Override
    public HealthDamageEvent fireDghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        var dghHealthDamageEvent = new HealthDamageEvent(source, sourceDamage, newDamage);
        NeoForge.EVENT_BUS.post(dghHealthDamageEvent);
        return dghHealthDamageEvent;
    }

    @Override
    public ComponentDamageEvent fireDghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        var event = new ComponentDamageEvent(blocking, component, damageAmount, resist, type);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    @Override
    public boolean onLivingUseTotem(LivingEntity entity, DamageSource damageSource, ItemStack totem, InteractionHand hand) {
        return !NeoForge.EVENT_BUS.post(new LivingUseTotemEvent(entity, damageSource, totem, hand)).isCanceled();
    }
}
