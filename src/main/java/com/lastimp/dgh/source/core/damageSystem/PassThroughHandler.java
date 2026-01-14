package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.OPEN_WOUND_RES;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.PASS_THROUGH;

public class PassThroughHandler {
    public static void handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(PASS_THROUGH, damageAmount);
        health.addDirectInjury(source.getEntity(), body.getComponent(), BodyCondition.get(PASS_THROUGH).getComponent(), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        handle(source, health, body, damageAmount);
        float damage = body.getConditionValue(PASS_THROUGH) + body.getConditionHidden(PASS_THROUGH);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
        FollowInjuryHandler.foreignObjectHandler(body, health, damageAmount, Config.bypass_foreign_prob);
        FollowInjuryHandler.brainDamageHandler(body, health, damageAmount, 0.05f, Config.bypass_brain_damage_prob);
    }
}
