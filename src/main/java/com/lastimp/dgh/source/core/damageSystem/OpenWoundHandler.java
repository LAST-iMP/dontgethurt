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

public class OpenWoundHandler {
    public static void handle(AbstractVisibleBody body, float damageAmount) {
        damageAmount *= (1.0f - body.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(OPEN_WOUND, damageAmount);
    }

    public static void handleEntityAttack(AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, damage);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
    }

    public static void handleExplosion(AbstractVisibleBody body, float damageAmount) {
        handle(body, damageAmount);
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body);
        FollowInjuryHandler.arterialBleedingHandler(body);
    }
}
