package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class OpenWoundHandler {
    public static void handle(AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(OPEN_WOUND, damageAmount);
    }

    public static void handleEntityAttack(LivingEntity entity, AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, damage);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(LivingEntity entity, AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }
}
