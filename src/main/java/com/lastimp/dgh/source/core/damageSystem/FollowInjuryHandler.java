package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public abstract class FollowInjuryHandler {
    public static void dislocationHandler(AbstractExtremities body, HealthCapability health, float damageAmount) {
        float factor = (0.9f - Config.baseDislocationThreshold) / Config.baseDislocationMaxProb;
        dislocationHandler(
                body, health, damageAmount,
                Config.baseDislocationThreshold, factor, 0, Config.baseDislocationMaxProb, body.fractCheckTimes()
        );
    }

    public static void dislocationHandler(AbstractExtremities body, HealthCapability health, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (!body.canHurtBone()) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            if (!body.isBadBandaged() && !body.isBadBandaged()) {
                body.setConditionValue(DISLOCATION, BodyCondition.get(DISLOCATION).maxValue());
                health.addDirectInjury(body.getComponent(), BodyCondition.get(DISLOCATION).getComponent(), 1);
            }
        }
    }

    public static void fractionHandler(AbstractVisibleBody body, HealthCapability health, float damageAmount) {
        float threshold = body.fractThreshold();
        float factor = (0.9f - threshold) / Config.baseFractureMaxProb;
        fractionHandler(body, health, damageAmount, threshold, factor, 0, Config.baseFractureMaxProb, body.fractCheckTimes());
    }

    public static void fractionHandler(AbstractVisibleBody body, HealthCapability health, float damageAmount, float threshold, float factor, float p_min, float p_max, int check) {
        if (!body.canHurtBone()) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max, check)) {
            body.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            health.addDirectInjury(body.getComponent(), BodyCondition.get(FRACTURE).getComponent(), 1);
            if (body.abnormal(PLASTER_CAST))
                body.setConditionValue(PLASTER_CAST, BodyCondition.get(PLASTER_CAST).defaultValue());
            arterialBleedingByFractionHandler(body, health);
        }
    }

    public static void arterialBleedingByFractionHandler(AbstractVisibleBody body, HealthCapability health) {
        if ((body instanceof AbstractExtremities || body instanceof Head) && Mth.randomBetween(Utils.randomSource, 0f, 1.0f) < Config.fractureArterialProb) {
            body.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
            health.addDirectInjury(body.getComponent(), BodyCondition.get(ARTERIAL_BLEEDING).getComponent(),  1);
        }
    }

    public static void arterialBleedingHandler(AbstractVisibleBody body, HealthCapability health) {
        if (body.getConditionValue(BLEED) > 0.8) {
            handleArterialBleeding(body, health, 1);
        }
    }

    private static void handleArterialBleeding(AbstractVisibleBody body, HealthCapability health, int level) {
        if ((body instanceof AbstractExtremities || body instanceof Head)) {
            body.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
            health.addDirectInjury(body.getComponent(), BodyCondition.get(ARTERIAL_BLEEDING).getComponent(),  level);
        } else if (body instanceof Torso torso) {
            torso.injury(AORTIC_RUPTURE, BodyCondition.get(AORTIC_RUPTURE).maxValue());
            health.addDirectInjury(body.getComponent(), BodyCondition.get(AORTIC_RUPTURE).getComponent(), level);
        }
    }

    public static void pneumothoraxHandler(AbstractVisibleBody body, HealthCapability health) {
        if (!(body instanceof Torso torso)) return;
        if (Mth.randomBetween(Utils.randomSource, 0f, 1.0f) > Config.basePneumothoraxProb) return;

        torso.injury(PNEUMOTHORAX, BodyCondition.get(PNEUMOTHORAX).maxValue());
        health.addDirectInjury(body.getComponent(), BodyCondition.get(PNEUMOTHORAX).getComponent(), 1);
    }

    public static void traumaticAmputationHandler(LivingEntity entity, AbstractVisibleBody body, HealthCapability health, float damageAmount, float threshold, float factor, float p_min, float p_max) {
        if (!(body instanceof AbstractExtremities)) return;
        if (!body.canHurtBone()) return;
        if (!body.abnormal(FRACTURE)) return;
        if (body.abnormal(TRAUMATIC_AMPUTATION)) return;

        if (Utils.randomCheck(damageAmount, threshold, factor, p_min, p_max)) {
            body.injury(TRAUMATIC_AMPUTATION, BodyCondition.get(TRAUMATIC_AMPUTATION).maxValue());
            body.injury(ARTERIAL_BLEEDING, BodyCondition.get(ARTERIAL_BLEEDING).maxValue());
            body.injury(DISLOCATION, -BodyCondition.get(DISLOCATION).maxValue());
            body.injury(FRACTURE, -BodyCondition.get(FRACTURE).maxValue());
            Item limb = body instanceof AbstractArm ? ModItems.HUMAN_HAND.get() : ModItems.HUMAN_LEG.get();
            Utils.drop(limb, entity, 1);
            health.addDirectInjury(body.getComponent(), BodyCondition.get(TRAUMATIC_AMPUTATION).getComponent(), 1);
            health.addDirectInjury(body.getComponent(), BodyCondition.get(ARTERIAL_BLEEDING).getComponent(),  1);
        }
    }

    public static void foreignObjectHandler(AbstractVisibleBody body, HealthCapability health, float currentDamage, float prob) {
        if (Utils.randomBetween(0, 1) > prob) return;

        float damage = Math.min(0.2f, currentDamage * 0.25f);
        body.injury(FOREIGN_OBJECT, damage);
        health.addDirectInjury(body.getComponent(), BodyCondition.get(FOREIGN_OBJECT).getComponent(), damage,  1);
        if (body.getConditionValue(FOREIGN_OBJECT) > 0.2f) {
            handleArterialBleeding(body, health, 1);
        }
    }

    public static void brainDamageHandler(AbstractVisibleBody body, HealthCapability health, float currentDamage, float threshold, float prob) {
        if (!(body instanceof Head head)) return;
        if (currentDamage < threshold) return;
        if (Utils.randomBetween(0, 1) > prob) return;

        float ratio = Utils.randomBetween(0.1f, 0.4f);
        head.injury(BRAIN_DAMAGE, currentDamage * ratio);
        health.addDirectInjury(body.getComponent(), BodyCondition.get(BRAIN_DAMAGE).getComponent(), currentDamage * ratio,  1);
    }
}
