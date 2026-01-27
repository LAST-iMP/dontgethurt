package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;

public class PassThroughHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        float block = Math.min(damageAmount, body.getConditionHidden(OPEN_WOUND_RES));
        damageAmount -= block;
        body.addConditionHidden(OPEN_WOUND_RES, -block);

        float resist = body.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max;
        resist += health.getComponent(BLOOD).getConditionValue(HARDENER) / 2;
        damageAmount *= (1.0f - Math.min(1, resist));
        body.injury(PASS_THROUGH, damageAmount);
        if (body.abnormal(CLAMP_PLATE)) body.setConditionValue(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).defaultValue());
        health.addDirectInjury(source.getEntity(), body.getComponent(), Component.literal("贯穿伤"), damageAmount);
        return damageAmount > 0;
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(PASS_THROUGH) + body.getConditionHidden(PASS_THROUGH);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
        FollowInjuryHandler.foreignObjectHandler(body, health, damageAmount, Config.bypass_foreign_prob);
        FollowInjuryHandler.brainDamageHandler(body, health, damageAmount, 0.05f, Config.bypass_brain_damage_prob);
    }
}
