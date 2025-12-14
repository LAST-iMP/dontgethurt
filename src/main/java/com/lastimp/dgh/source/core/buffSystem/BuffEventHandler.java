package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;


@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BuffEventHandler {
    @SubscribeEvent
    public static void onBuffUpdate(EntityTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        var entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity) || !HealthCapability.has(livingEntity)) return;
        HealthCapability health = HealthCapability.get(livingEntity);

        updateStaggerEffects(health, livingEntity);
        updateArmEffects(health, livingEntity);
        updateWithdrawEffects(health, livingEntity);
        updateLivingTimeEffects(health, livingEntity);
        updateCureEffects(health, livingEntity);
        updateSymptomsEffects(health, livingEntity);
    }

    private static void updateStaggerEffects(HealthCapability health, LivingEntity entity) {
        if (health.slowDown() > 0 && !entity.hasEffect(ModEffects.STAGGER_EFFECT)) {
            var newEffect = new MobEffectInstance(
                    ModEffects.STAGGER_EFFECT,
                    40, health.slowDown() - 1
            );
            entity.addEffect(newEffect);
        }
    }

    private static void updateArmEffects(HealthCapability health, LivingEntity entity) {
        if (health.armBreak() == 0) return;

        var newEffect = new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                40, health.armBreak() - 1
        );
        if (!entity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            entity.addEffect(newEffect);
        } else if (entity.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier() >= health.armBreak()) {
            entity.getEffect(MobEffects.DIG_SLOWDOWN).update(newEffect);
        }
    }

    private static void updateWithdrawEffects(HealthCapability health, LivingEntity entity) {
        var head = health.getComponent(HEAD);
        var state = head.getCondition(WITHDRAW);
        if (!head.abnormal(WITHDRAW)) return;
        if (state.getValue() > 0.2f && !entity.hasEffect(ModEffects.CRAVING_EFFECT)) {
            entity.addEffect(new MobEffectInstance(ModEffects.CRAVING_EFFECT, 100));
        }
        if (state.getValue() > 0.3f && !entity.hasEffect(ModEffects.SWEATING_EFFECT)) {
            entity.addEffect(new MobEffectInstance(ModEffects.SWEATING_EFFECT, 100));
        }
        if (state.getValue() > 0.4f && !entity.hasEffect(MobEffects.CONFUSION)) {
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
        }
    }

    private static void updateLivingTimeEffects(HealthCapability health, LivingEntity entity) {
        if (entity.hasEffect(ModEffects.KEEP_LIVING_EFFECT)) return;
        int amp = (int) Math.sqrt((double) health.livingTick() / 1000);
        amp = Math.min(amp, 40);
        if (amp < 1) return;
        var newEffect = new MobEffectInstance(
                ModEffects.KEEP_LIVING_EFFECT,
                100, amp - 1,
                false, false, true
        );
        entity.addEffect(newEffect);
    }

    private static void updateCureEffects(HealthCapability health, LivingEntity entity) {
        if (entity.hasEffect(ModEffects.CURE_EFFECT)) return;
        if (health.vitality() < 0.999f) return;
        if (health.almostDead() < 0.2f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 2));
        } else if (health.almostDead() < 0.5f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 1));
        } else if (health.almostDead() < 0.8f) {
            entity.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 0));
        } else {
            return;
        }
        health.resetAlmostDead();
    }

    private static void updateSymptomsEffects(HealthCapability health, LivingEntity entity) {
        if (!entity.hasEffect(ModEffects.INTENSE_PAIN_EFFECT) && health.intensePain()) {
            if (Mth.randomBetween(Utils.randomSource, 0.0f, 1.0f) < 0.007f) {
                var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT, 60);
                entity.addEffect(newEffect);
            }
        }
        if (!entity.hasEffect(ModEffects.PALE_SKIN) && health.getComponent(BLOOD).getConditionValue(BLOOD_LOSS) > 0.4f) {
            entity.addEffect(new MobEffectInstance(ModEffects.PALE_SKIN, 100));
        }
        if (!entity.hasEffect(ModEffects.HARD_BREATH)) {
            if (health.getComponent(BLOOD).getConditionValue(OXYGEN) > 0.2f || health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST))
                entity.addEffect(new MobEffectInstance(ModEffects.HARD_BREATH, 100));
        }
        if (!entity.hasEffect(MobEffects.BLINDNESS)) {
            if (health.getComponent(BLOOD).getConditionValue(OXYGEN) > 0.5f)
                entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
        }
        if (!entity.hasEffect(MobEffects.WEAKNESS)) {
            var value = health.getComponent(BLOOD).getConditionValue(BLOOD_LOSS);
            if (value > 0.6f)
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100,
                        (int) ((value - 0.6f) / 0.2f)
                ));
        }
    }
}
