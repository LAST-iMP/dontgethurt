package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static PlayerHealthCapability handle(PlayerHealthCapability health, AbstractBody body, float damageAmount) {
        body.injury(INTERNAL_INJURY, damageAmount);
        return health;
    }

    public static PlayerHealthCapability handleBluntTrauma(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return health;

        if (Utils.randomCheck(damageAmount, 4.0f / maxHealth, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
        if (Utils.randomCheck(damageAmount, 2.0f / maxHealth, 0.8f, 0.0f, 0.5f)) {
            if (!FRACTURE.abnormal(body.getConditionValue(FRACTURE)) && !extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, DISLOCATION.maxValue);
        }
        return health;
    }

    public static PlayerHealthCapability handleExplosion(PlayerHealthCapability health, AbstractBody body, float damageAmount, float maxHealth) {
        handle(health, body, damageAmount);
        if (!(body instanceof AbstractExtremities extremities)) return health;

        if (Utils.randomCheck(damageAmount, 2.0f / maxHealth, 0.6f, 0.0f, 1.0f)) {
            extremities.setConditionValue(FRACTURE, FRACTURE.maxValue);
            if (PLASTER_CAST.abnormal(body.getConditionValue(PLASTER_CAST)))
                body.setConditionValue(PLASTER_CAST, PLASTER_CAST.defaultValue);
        }
        if (Utils.randomCheck(damageAmount, 0, 1.0f, 0.35f, 0.36f)) {
            if (!extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, DISLOCATION.maxValue);
        }
        return health;
    }
}
