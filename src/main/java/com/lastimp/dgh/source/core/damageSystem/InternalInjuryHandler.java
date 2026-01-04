package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static void handle(AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(INTERNAL_RES) * Config.resistance_max);
        body.injury(INTERNAL_INJURY, damageAmount);
    }

    public static void handleBluntTrauma(LivingEntity entity, AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, damage);
        FollowInjuryHandler.fractionHandler(body, damage);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(LivingEntity entity, AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, damage, 0, 1, 0.35f, 0.36f, 0);
        FollowInjuryHandler.fractionHandler(body, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }
}
