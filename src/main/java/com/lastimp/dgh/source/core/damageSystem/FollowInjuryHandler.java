package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.DISLOCATION;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.FRACTURE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.PLASTER_CAST;

public abstract class FollowInjuryHandler {
    public static void dislocationHandler(AbstractExtremities body, float damageAmount) {
        float factor = (0.9f - Config.baseDislocationThreshold) / Config.baseDislocationMaxProb;
        dislocationHandler(body, damageAmount, Config.baseDislocationThreshold, factor, 0, Config.baseDislocationMaxProb, body.fractCheckTimes());
    }

    public static void dislocationHandler(AbstractExtremities body, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (!body.canHurtBone()) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            if (!body.isBadBandaged() && !body.isBadBandaged())
                body.setConditionValue(DISLOCATION, BodyCondition.get(DISLOCATION).maxValue());
        }
    }

    public static void fractionHandler(AbstractVisibleBody body, float damageAmount) {
        float threshold = body.fractThreshold();
        float factor = (0.9f - threshold) / Config.baseFractureMaxProb;
        fractionHandler(body, damageAmount, threshold, factor, 0, Config.baseFractureMaxProb, body.fractCheckTimes());
    }

    public static void fractionHandler(AbstractVisibleBody body, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (!body.canHurtBone()) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            body.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            if (body.abnormal(PLASTER_CAST))
                body.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
            arterialBleedingByFractionHandler(body);
        }
    }

    public static void arterialBleedingByFractionHandler(AbstractVisibleBody body) {
        if (body instanceof AbstractExtremities extremities && Mth.randomBetween(Utils.randomSource, 0f, 1.0f) < Config.fractureArterialProb) {
            extremities.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
        }
    }

    public static void arterialBleedingHandler(AbstractVisibleBody body) {
        float bleed = body.getConditionValue(BLEED);
        if (body instanceof AbstractExtremities extremities && bleed > 0.8) {
            extremities.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
        } else if (body instanceof Torso torso && bleed > 0.8) {
            torso.injury(AORTIC_RUPTURE, BodyCondition.get(AORTIC_RUPTURE).maxValue());
        }
    }

    public static void pneumothoraxHandler(AbstractVisibleBody body) {
        if (!(body instanceof Torso torso)) return;
        if (Mth.randomBetween(Utils.randomSource, 0f, 1.0f) > Config.basePneumothoraxProb) return;

        torso.injury(PNEUMOTHORAX, BodyCondition.get(PNEUMOTHORAX).maxValue());
    }

    public static void traumaticAmputationHandler(LivingEntity entity, AbstractVisibleBody body, float damageAmount, float threshold, float factor, float p_min, float p_max) {
        if (!(body instanceof AbstractExtremities)) return;
        if (!body.canHurtBone()) return;
        if (!body.abnormal(FRACTURE)) return;
        if (body.abnormal(TRAUMATIC_AMPUTATION)) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max)) {
            body.injury(TRAUMATIC_AMPUTATION, BodyCondition.get(TRAUMATIC_AMPUTATION).maxValue());
            body.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
            Item limb = body instanceof AbstractArm ? ModItems.HUMAN_HAND.get() : ModItems.HUMAN_LEG.get();
            Utils.drop(limb, entity, 1);;
        }
    }
}
