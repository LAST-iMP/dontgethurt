package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.CLAMP_PLATE;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;

public class BurnHandler {
    public static void handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        float block = Math.min(damageAmount, body.getConditionHidden(BURN_RES));
        damageAmount -= block;
        body.addConditionHidden(BURN_RES, -block);

        float resist = body.getConditionValue(BURN_RES) * Config.resistance_max;
        resist += health.getComponent(BLOOD).getConditionValue(HARDENER) / 2;
        damageAmount *= (1.0f - Math.min(1, resist));

        body.injury(BURN, damageAmount);
        if (body.abnormal(CLAMP_PLATE)) body.setConditionValue(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).defaultValue());
        health.addDirectInjury(source.getEntity(), body.getComponent(), Component.literal("烧伤"), damageAmount);
    }
}
