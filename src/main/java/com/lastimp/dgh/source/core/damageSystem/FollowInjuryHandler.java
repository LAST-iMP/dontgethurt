package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.Utils;
import net.minecraft.util.Mth;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.DISLOCATION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.FRACTURE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.PLASTER_CAST;

public abstract class FollowInjuryHandler {
    public static void dislocationHandler(AbstractExtremities body, float damageAmount) {
        if (body.abnormal(SAWED_BONES)) return;
        float factor = (0.9f - Config.baseDislocationThreshold) / Config.baseDislocationMaxProb;
        dislocationHandler(body, damageAmount, Config.baseDislocationThreshold, factor, 0, Config.baseDislocationMaxProb, body.fractCheckTimes());
    }

    public static void dislocationHandler(AbstractExtremities body, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (body.abnormal(SAWED_BONES)) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            if (!body.isBadBandaged() && !body.isBadBandaged())
                body.setConditionValue(DISLOCATION, BodyCondition.get(DISLOCATION).maxValue());
        }
    }

    public static void fractionHandler(AbstractVisibleBody body, float damageAmount) {
        if (body.abnormal(SAWED_BONES)) return;
        float threshold = body.fractThreshold();
        float factor = (0.9f - threshold) / Config.baseFractureMaxProb;
        fractionHandler(body, damageAmount, threshold, factor, 0, Config.baseFractureMaxProb, body.fractCheckTimes());
    }

    public static void fractionHandler(AbstractVisibleBody body, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (body.abnormal(SAWED_BONES)) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            body.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (body.abnormal(PLASTER_CAST))
                body.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
            if (body instanceof AbstractExtremities extremities)
                handleArterialBleeding(extremities);
        }
    }

    public static void handleArterialBleeding(AbstractExtremities body) {
        if (Mth.randomBetween(Utils.randomSource, 0f, 1.0f) < Config.fractureArterialProb) {
            body.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
        }
    }
}
