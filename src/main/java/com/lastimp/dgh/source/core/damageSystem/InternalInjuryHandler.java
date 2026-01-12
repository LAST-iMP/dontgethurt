package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static void handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(INTERNAL_RES) * Config.resistance_max);
        body.injury(INTERNAL_INJURY, damageAmount);
        health.addDirectInjury(source.getEntity(), body.getComponent(), BodyCondition.get(INTERNAL_INJURY).getComponent(), damageAmount);
    }

    public static void handleBluntTrauma(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        handle(source, health, body, damageAmount);
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        handle(source, health, body, damageAmount);
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage, 0, 1, 0.35f, 0.36f, 0);
        FollowInjuryHandler.fractionHandler(body, health, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }
}
