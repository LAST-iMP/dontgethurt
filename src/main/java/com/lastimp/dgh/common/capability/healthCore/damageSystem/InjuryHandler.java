package com.lastimp.dgh.common.capability.healthCore.damageSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

public class InjuryHandler {
    public static boolean handleDirect(Entity entity, HealthCapability health, AbstractBody body, ResourceLocation damageType, Component name, float damageAmount) {
        var event = PlatformService.EVENT_HOOK.fireDghComponentDamageEvent(0, body.getBodyType(), damageAmount, 0, damageType);
        if (event.isCanceled()) return false;
        damageAmount -= event.block();
        damageAmount *= (1.0f - Math.min(1, event.resist()));

        body.injury(damageType, damageAmount);
        health.addDirectInjury(entity, body.getComponent(), name, damageAmount);
        return damageAmount > 0;
    }

    public static boolean handle(Entity entity, HealthCapability health, AbstractBody body, ResourceLocation damageType, ResourceLocation resistType, Component name, float damageAmount) {
        float block = Math.min(damageAmount, body.getConditionHidden(resistType));
        float resist = body.getConditionValue(resistType) * PlatformService.CONFIG.RESISTANCE_MAX();
        resist += health.getComponent(BLOOD).getConditionValue(HARDENER) / 2;

        var event = PlatformService.EVENT_HOOK.fireDghComponentDamageEvent(block, body.getBodyType(), damageAmount, resist, damageType);
        if (event.isCanceled()) return false;
        damageAmount -= event.block();
        body.addConditionHidden(resistType, -event.block());
        damageAmount *= (1.0f - Math.min(1, event.resist()));

        body.injury(damageType, damageAmount);
        if (body.abnormal(CLAMP_PLATE)) body.setConditionValue(CLAMP_PLATE, ConditionAccessor.get(CLAMP_PLATE).defaultValue());
        health.addDirectInjury(entity, body.getComponent(), name, damageAmount);
        return damageAmount > 0;
    }
}
