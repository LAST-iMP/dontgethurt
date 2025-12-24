package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Torso;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static void handle(AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(INTERNAL_RES) * Config.resistance_max);
        body.injury(INTERNAL_INJURY, damageAmount);
    }

    public static void handleBluntTrauma(AbstractVisibleBody visibleBody, float damageAmount) {
        handle(visibleBody, damageAmount);
        float damage = visibleBody.getConditionValue(INTERNAL_INJURY) + visibleBody.getConditionHidden(INTERNAL_INJURY);
        if (visibleBody instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, damage);
        FollowInjuryHandler.fractionHandler(visibleBody, damage);
        FollowInjuryHandler.pneumothoraxHandler(visibleBody);
    }

    public static void handleExplosion(AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, damage, 0, 1, 0.35f, 0.36f, 0);
        FollowInjuryHandler.fractionHandler(body, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body);
    }
}
