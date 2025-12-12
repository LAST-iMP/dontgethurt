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
    public static void handle(AbstractBody body, float damageAmount) {
        if (body instanceof AbstractVisibleBody visibleBody)
            damageAmount *= (1.0f - visibleBody.getConditionValue(INTERNAL_RES) * Config.resistance_max);
        body.injury(INTERNAL_INJURY, damageAmount);
    }

    public static void handleBluntTrauma(AbstractBody body, float damageAmount) {
        handle(body, damageAmount);
        if (!(body instanceof AbstractVisibleBody visibleBody)) return;

        float threshold = visibleBody.fractThreshold();
        float factor = (1.0f - threshold) / Config.baseFractureMaxProb;

        damageAmount += body.getCondition(INTERNAL_INJURY).getValue();
        if (Utils.randomCheck(damageAmount, threshold, factor, 0.0f, Config.baseFractureMaxProb, visibleBody.fractCheckTimes())) {
            visibleBody.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (visibleBody.abnormal(PLASTER_CAST))
                visibleBody.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
        }

        factor = (1.0f - Config.baseDislocationThreshold) / Config.baseDislocationMaxProb;
        if (!(visibleBody instanceof AbstractExtremities extremities)) return;
        if (Utils.randomCheck(damageAmount, Config.baseDislocationThreshold, factor, 0.0f, Config.baseDislocationMaxProb, visibleBody.fractCheckTimes())) {
            if (!extremities.abnormal(FRACTURE) && !extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, BodyCondition.get(DISLOCATION).maxValue());
        }
    }

    public static void handleExplosion(AbstractBody body, float damageAmount) {
        handle(body, damageAmount);
        if (!(body instanceof AbstractVisibleBody visibleBody)) return;

        float factor = 1.0f - Config.baseFractureThreshold;
        damageAmount += body.getCondition(INTERNAL_INJURY).getValue();
        if (Utils.randomCheck(damageAmount, Config.baseFractureThreshold, factor, 0.0f, 1.0f)) {
            visibleBody.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (visibleBody.abnormal(PLASTER_CAST))
                visibleBody.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
        }

        if (!(visibleBody instanceof AbstractExtremities extremities)) return;
        if (Utils.randomCheck(damageAmount, 0, 1.0f, 0.35f, 0.36f)) {
            if (!extremities.isBadBandaged() && !extremities.isBadBandaged())
                extremities.setConditionValue(DISLOCATION, BodyCondition.get(DISLOCATION).maxValue());
        }
    }
}
