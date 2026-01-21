package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class OpenWoundHandler {
    public static void handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(OPEN_WOUND, damageAmount);
        if (body.abnormal(CLAMP_PLATE)) body.setConditionValue(CLAMP_PLATE, BodyCondition.get(CLAMP_PLATE).defaultValue());
        health.addDirectInjury(source.getEntity(), body.getComponent(), BodyCondition.get(OPEN_WOUND).getComponent(), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        handle(source, health, body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        handle(source, health, body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.1f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }
}
