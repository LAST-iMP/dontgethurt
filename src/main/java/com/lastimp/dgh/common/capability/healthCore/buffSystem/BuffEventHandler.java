package com.lastimp.dgh.common.capability.healthCore.buffSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.common.enums.BodyComponents.*;
import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class BuffEventHandler {
    public static void onBuffUpdate(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide()) return;
        updateDyingEffects(livingEntity);
        if (!HealthCapability.has(livingEntity)) return;
        HealthCapability.getAndApply(livingEntity, health -> {
            updateStaggerEffects(health, livingEntity);
            updateArmEffects(health, livingEntity);
            updateWithdrawEffects(health, livingEntity);
            updateLivingTimeEffects(health, livingEntity);
            updateCureEffects(health, livingEntity);
            updateSymptomsEffects(health, livingEntity);
        });
    }

    private static void updateDyingEffects(LivingEntity entity) {
        var isDown = HealthCapability.isDown(entity);
        if (isDown && PlatformService.CONFIG.PLAYER_GLOWING())
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 19));
    }

    private static void updateStaggerEffects(HealthCapability health, LivingEntity entity) {
        if (health.slowDown() <= 0) return;
        var newEffect = new MobEffectInstance(
                ModEffects.STAGGER_EFFECT.get(),
                39, health.slowDown() - 1
        );
        entity.addEffect(newEffect);
    }

    private static void updateArmEffects(HealthCapability health, LivingEntity entity) {
        if (health.armBreak() == 0) return;

        var newEffect = new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                39, health.armBreak() - 1
        );
        entity.addEffect(newEffect);
    }

    private static void updateWithdrawEffects(HealthCapability health, LivingEntity entity) {
        var head = health.getComponent(HEAD);
        var state = head.getCondition(WITHDRAW);
        if (!head.abnormal(WITHDRAW)) return;
        if (state.getValue() > 0.2f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CRAVING_EFFECT.get(), 99));
        }
        if (state.getValue() > 0.3f) {
            entity.addEffect(new MobEffectInstance(ModEffects.SWEATING_EFFECT.get(), 99));
        }
        if (state.getValue() > 0.4f) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 199));
        }
    }

    private static void updateLivingTimeEffects(HealthCapability health, LivingEntity entity) {
        if (!PlatformService.CONFIG.ENABLE_LIVING_EFFECT()) return;
        int amp = (int) Math.sqrt((double) health.livingTick() / 1000);
        amp = Math.min(amp, 39);
        if (amp < 1) return;
        var newEffect = new MobEffectInstance(
                ModEffects.KEEP_LIVING_EFFECT.get(),
                100, amp - 1,
                false, false, true
        );
        entity.addEffect(newEffect);
    }

    private static void updateCureEffects(HealthCapability health, LivingEntity entity) {
        if (health.vitality() < 0.999f) return;
        if (health.almostDead() < -0.1f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2399, 3));
        } else if (health.almostDead() < 0.2f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2399, 2));
        } else if (health.almostDead() < 0.5f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2399, 1));
        } else if (health.almostDead() < 0.8f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2399, 0));
        } else {
            return;
        }
        health.resetAlmostDead();
    }

    private static void updateSymptomsEffects(HealthCapability health, LivingEntity entity) {
        if (health.intensePain() && Mth.randomBetween(Utils.randomSource, 0.0f, 1.0f) < 0.007f) {
            var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT.get(), 59);
            entity.addEffect(newEffect);
        }
        var blood = health.getComponent(BLOOD);
        var torso = health.getComponent(TORSO);
        if (blood.getConditionValue(BLOOD_LOSS) > 0.4f || blood.getConditionValue(BLOOD_PRESSURE) < 0.5)
            entity.addEffect(new MobEffectInstance(ModEffects.PALE_SKIN_EFFECT.get(), 99));
        if (blood.getConditionValue(OXYGEN) > 0.2f || torso.abnormal(RESPIRATORY_ARREST) || torso.getConditionValue(PNEUMOTHORAX) > 0.4)
            entity.addEffect(new MobEffectInstance(ModEffects.HARD_BREATH_EFFECT.get(), 99));
        if (!torso.abnormal(ANALGESIA)) {
            if (blood.getConditionValue(OXYGEN) > 0.5f || blood.getConditionValue(SEPSIS) > 0.4 || blood.getConditionValue(BLOOD_PRESSURE) < 0.6)
                entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 99));
            if (blood.getConditionValue(BLOOD_PRESSURE) < 0.3f)
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 99));
            var bloodLoss = blood.getConditionValue(BLOOD_LOSS);
            if (bloodLoss > 0.6f)
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 99, (int) ((bloodLoss - 0.6f) / 0.2f)));
        }
        if (torso.abnormal(HEARTRATE_INCREASE))
            entity.addEffect(new MobEffectInstance(ModEffects.INCREASED_HEARTRATE_EFFECT.get(), 99));
        if (health.isInfected())
            entity.addEffect(new MobEffectInstance(ModEffects.INFLAMMATION_EFFECT.get(), 99));
        if (blood.getConditionValue(SEPSIS) > 0.05)
            entity.addEffect(new MobEffectInstance(ModEffects.FEVER_EFFECT.get(), 99));
    }
}
