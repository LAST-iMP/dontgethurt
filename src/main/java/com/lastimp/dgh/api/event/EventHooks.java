package com.lastimp.dgh.api.event;

import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.NeoForge;

public class EventHooks {
    public static DghHealthDamageEvent fireDghHealthDamageEvent(DamageSource source, float sourceDamage, float newDamage) {
        var dghHealthDamageEvent = new DghHealthDamageEvent(source, sourceDamage, newDamage);
        NeoForge.EVENT_BUS.post(dghHealthDamageEvent);
        return dghHealthDamageEvent;
    }

    public static DghComponentDamageEvent fireDghComponentDamageEvent(float blocking, BodyComponents component, float damageAmount, float resist, ResourceLocation type) {
        var event = new DghComponentDamageEvent(blocking, component, damageAmount, resist, type);
        NeoForge.EVENT_BUS.post(event);
        return event;
    }

    public static DghBodyConditionRegisterEvent fireDghBodyConditionRegisterEvent() {
        var event = new DghBodyConditionRegisterEvent();
        NeoForge.EVENT_BUS.post(event);
        return event;
    }
}
