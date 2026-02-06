package com.lastimp.dgh.forge.event;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.event.IComponentDamageEvent;
import com.lastimp.dgh.common.event.IEventHook;
import com.lastimp.dgh.common.event.IHealthDamageEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingUseTotemEvent;

public class EventHooksNF implements IEventHook {
    public IHealthDamageEvent fireDghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        var dghHealthDamageEvent = new HealthDamageEvent(source, sourceDamage, newDamage);
        MinecraftForge.EVENT_BUS.post(dghHealthDamageEvent);
        return dghHealthDamageEvent;
    }

    public IComponentDamageEvent fireDghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        var event = new ComponentDamageEvent(blocking, component, damageAmount, resist, type);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }

    @Override
    public boolean onLivingUseTotem(LivingEntity entity, DamageSource damageSource, ItemStack totem, InteractionHand hand) {
        return !MinecraftForge.EVENT_BUS.post(new LivingUseTotemEvent(entity, damageSource, totem, hand));
    }
}
