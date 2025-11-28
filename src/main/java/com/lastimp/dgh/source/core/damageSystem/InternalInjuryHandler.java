package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static void handle(PlayerHealthCapability health, AbstractBody body, float damageAmount) {
        if (body instanceof AbstractVisibleBody visibleBody)
            damageAmount *= (1.0f - visibleBody.getConditionValue(INTERNAL_RES) * Config.resistance_max);
        body.injury(INTERNAL_INJURY, damageAmount);
    }

    public static void handleBluntTrauma(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return;

        damageAmount += body.getCondition(INTERNAL_INJURY).getValue();
        if (Utils.randomCheck(damageAmount, 0.2f, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
        if (Utils.randomCheck(damageAmount, 0.1f, 0.8f, 0.0f, 0.5f)) {
            if (!FRACTURE.abnormal(body.getConditionValue(FRACTURE)) && !extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, DISLOCATION.maxValue);
        }
    }

    public static void handleExplosion(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return;

        damageAmount += body.getCondition(INTERNAL_INJURY).getValue();
        if (Utils.randomCheck(damageAmount, 0.1f, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
        if (Utils.randomCheck(damageAmount, 0, 1.0f, 0.35f, 0.36f)) {
            if (!extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, DISLOCATION.maxValue);
        }
    }
}
