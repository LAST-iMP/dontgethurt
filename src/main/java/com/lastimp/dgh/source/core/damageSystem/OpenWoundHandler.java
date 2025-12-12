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
    public static void handle(AbstractBody body, float damageAmount) {
        if (body instanceof AbstractVisibleBody visibleBody)
            damageAmount *= (1.0f - visibleBody.getConditionValue(OPEN_WOUND_RES) * Config.resistance_max);
        body.injury(OPEN_WOUND, damageAmount);
    }

    public static void handleEntityAttack(AbstractBody body, float damageAmount) {
        handle(body, damageAmount);
        if (!(body instanceof AbstractVisibleBody visibleBody)) return;
        if (body.abnormal(SAWED_BONES)) return;

        float threshold = visibleBody.fractThreshold();
        float factor = (1.0f - threshold) / Config.baseFractureMaxProb;

        damageAmount += body.getCondition(OPEN_WOUND).getValue();
        if (Utils.randomCheck(damageAmount, threshold, factor, 0.0f, Config.baseFractureMaxProb, visibleBody.fractCheckTimes())) {
            visibleBody.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (visibleBody.abnormal(PLASTER_CAST))
                visibleBody.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
        }
    }

    public static void handleExplosion(AbstractBody body, float damageAmount) {
        handle(body, damageAmount);
        if (!(body instanceof AbstractExtremities visibleBody)) return;
        if (body.abnormal(SAWED_BONES)) return;

        float factor = 1.0f - Config.baseFractureThreshold;
        damageAmount += body.getCondition(OPEN_WOUND).getValue();
        if (Utils.randomCheck(damageAmount, Config.baseFractureThreshold, factor, 0.0f, 1.0f, visibleBody.fractCheckTimes())) {
            visibleBody.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (visibleBody.abnormal(PLASTER_CAST))
                visibleBody.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
        }
    }
}
