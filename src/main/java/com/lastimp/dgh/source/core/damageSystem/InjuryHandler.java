package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.event.EventHooks;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.CLAMP_PLATE;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;

public class InjuryHandler {
    public static boolean handleDirect(Entity entity, HealthCapability health, AbstractBody body, ResourceLocation damageType, Component name, float damageAmount) {
        var event = EventHooks.fireDghComponentDamageEvent(0, body.getBodyType(), damageAmount, 0, damageType);
        if (event.isCanceled()) return false;
        damageAmount -= event.block();
        damageAmount *= (1.0f - Math.min(1, event.resist()));

        body.injury(damageType, damageAmount);
        health.addDirectInjury(entity, body.getComponent(), name, damageAmount);
        return damageAmount > 0;
    }

    public static boolean handle(Entity entity, HealthCapability health, AbstractBody body, ResourceLocation damageType, ResourceLocation resistType, Component name, float damageAmount) {
        float block = Math.min(damageAmount, body.getConditionHidden(resistType));
        float resist = body.getConditionValue(resistType) * Config.resistance_max;
        resist += health.getComponent(BLOOD).getConditionValue(HARDENER) / 2;

        var event = EventHooks.fireDghComponentDamageEvent(block, body.getBodyType(), damageAmount, resist, damageType);
        if (event.isCanceled()) return false;
        damageAmount -= event.block();
        body.addConditionHidden(resistType, -event.block());
        damageAmount *= (1.0f - Math.min(1, event.resist()));

        body.injury(damageType, damageAmount);
        if (body.abnormal(CLAMP_PLATE)) body.setConditionValue(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).defaultValue());
        health.addDirectInjury(entity, body.getComponent(), name, damageAmount);
        return damageAmount > 0;
    }
}
