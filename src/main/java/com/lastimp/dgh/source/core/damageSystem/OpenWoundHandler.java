package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class OpenWoundHandler {
    public static void handle(PlayerHealthCapability health, AbstractBody body, float damageAmount) {
        if (body instanceof AbstractVisibleBody visibleBody)
            damageAmount *= (1.0f - visibleBody.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(OPEN_WOUND, damageAmount);
    }

    public static void handleEntityAttack(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return;

        if (Utils.randomCheck(damageAmount, 4.0f / maxHealth, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
    }

    public static void handleExplosion(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return;

        if (Utils.randomCheck(damageAmount, 2.0f / maxHealth, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
    }
}
