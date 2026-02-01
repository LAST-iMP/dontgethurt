package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BLOOD_LOSS;


@EventBusSubscriber(modid = DontGetHurt.MODID)
public class BuffEventHandler {
    @SubscribeEvent
    public static void onBuffUpdate(EntityTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        var entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;
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
        var isDying = HealthCapability.isDying(entity);
        if (isDying && Config.player_glowing)
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 19));
    }

    private static void updateStaggerEffects(HealthCapability health, LivingEntity entity) {
        if (health.slowDown() <= 0) return;
        var newEffect = new MobEffectInstance(
                ModEffects.STAGGER_EFFECT,
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
            entity.addEffect(new MobEffectInstance(ModEffects.CRAVING_EFFECT, 99));
        }
        if (state.getValue() > 0.3f) {
            entity.addEffect(new MobEffectInstance(ModEffects.SWEATING_EFFECT, 99));
        }
        if (state.getValue() > 0.4f) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 199));
        }
    }

    private static void updateLivingTimeEffects(HealthCapability health, LivingEntity entity) {
        if (!Config.enable_living_effect) return;
        int amp = (int) Math.sqrt((double) health.livingTick() / 1000);
        amp = Math.min(amp, 39);
        if (amp < 1) return;
        var newEffect = new MobEffectInstance(
                ModEffects.KEEP_LIVING_EFFECT,
                100, amp - 1,
                false, false, true
        );
        entity.addEffect(newEffect);
    }

    private static void updateCureEffects(HealthCapability health, LivingEntity entity) {
        if (health.vitality() < 0.999f) return;
        if (health.almostDead() < -0.1f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2399, 3));
        } else if (health.almostDead() < 0.2f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2399, 2));
        } else if (health.almostDead() < 0.5f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2399, 1));
        } else if (health.almostDead() < 0.8f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2399, 0));
        } else {
            return;
        }
        health.resetAlmostDead();
    }

    private static void updateSymptomsEffects(HealthCapability health, LivingEntity entity) {
        if (health.intensePain() && Mth.randomBetween(Utils.randomSource, 0.0f, 1.0f) < 0.007f) {
            var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT, 59);
            entity.addEffect(newEffect);
        }
        var blood = health.getComponent(BLOOD);
        var torso = health.getComponent(TORSO);
        if (blood.getConditionValue(BLOOD_LOSS) > 0.4f || blood.getConditionValue(BLOOD_PRESSURE) < 0.5)
            entity.addEffect(new MobEffectInstance(ModEffects.PALE_SKIN_EFFECT, 99));
        if (blood.getConditionValue(OXYGEN) > 0.2f || torso.abnormal(RESPIRATORY_ARREST) || torso.getConditionValue(PNEUMOTHORAX) > 0.4)
            entity.addEffect(new MobEffectInstance(ModEffects.HARD_BREATH_EFFECT, 99));
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
            entity.addEffect(new MobEffectInstance(ModEffects.INCREASED_HEARTRATE_EFFECT, 99));
        if (health.isInfected())
            entity.addEffect(new MobEffectInstance(ModEffects.INFLAMMATION_EFFECT, 99));
        if (blood.getConditionValue(SEPSIS) > 0.05)
            entity.addEffect(new MobEffectInstance(ModEffects.FEVER_EFFECT, 99));
    }
}
