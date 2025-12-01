package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyCondition.BLOOD_LOSS;


@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BuffEventHandler {
    @SubscribeEvent
    public static void onBuffUpdate(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.player;
        PlayerHealthCapability health = PlayerHealthCapability.get(player);

        updateStaggerEffects(health, player);
        updateArmEffects(health, player);
        updateWithdrawEffects(health, player);
        updateLivingTimeEffects(health, player);
        updateCureEffects(health, player);
        updateSymptomsEffects(health, player);
    }

    private static void updateStaggerEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (health.slowDown() > 0 && !player.hasEffect(ModEffects.STAGGER_EFFECT.get())) {
            var newEffect = new MobEffectInstance(
                    ModEffects.STAGGER_EFFECT.get(),
                    40, health.slowDown() - 1
            );
            player.addEffect(newEffect);
        }
    }

    private static void updateArmEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (health.armBreak() == 0) return;

        var newEffect = new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                40, health.armBreak() - 1
        );
        if (!player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            player.addEffect(newEffect);
        } else if (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier() >= health.armBreak()) {
            player.getEffect(MobEffects.DIG_SLOWDOWN).update(newEffect);
        }
    }

    private static void updateWithdrawEffects(PlayerHealthCapability health, ServerPlayer player) {
        var state = health.getComponent(HEAD).getCondition(WITHDRAW);
        if (!WITHDRAW.abnormal(state.getValue())) return;
        if (state.getValue() > 0.2f && !player.hasEffect(ModEffects.CRAVING_EFFECT.get())) {
            player.addEffect(new MobEffectInstance(ModEffects.CRAVING_EFFECT.get(), 100));
        }
        if (state.getValue() > 0.3f && !player.hasEffect(ModEffects.SWEATING_EFFECT.get())) {
            player.addEffect(new MobEffectInstance(ModEffects.SWEATING_EFFECT.get(), 100));
        }
        if (state.getValue() > 0.4f && !player.hasEffect(MobEffects.CONFUSION)) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
        }
    }

    private static void updateLivingTimeEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (player.hasEffect(ModEffects.KEEP_LIVING_EFFECT.get())) return;
        int amp = (int) Math.sqrt((double) health.livingTick() / 1000);
        amp = Math.min(amp, 40);
        if (amp < 1) return;
        var newEffect = new MobEffectInstance(
                ModEffects.KEEP_LIVING_EFFECT.get(),
                100, amp - 1,
                false, false, true
        );
        player.addEffect(newEffect);
    }

    private static void updateCureEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (player.hasEffect(ModEffects.CURE_EFFECT.get())) return;
        if (health.playerVitality() < 0.999f) return;
        if (health.almostDead() < 0.2f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2400, 2));
        } else if (health.almostDead() < 0.5f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2400, 1));
        } else if (health.almostDead() < 0.8f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT.get(), 2400, 0));
        } else {
            return;
        }
        health.resetAlmostDead();
    }

    private static void updateSymptomsEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (!player.hasEffect(ModEffects.INTENSE_PAIN_EFFECT.get()) && health.intensePain()) {
            if (Mth.randomBetween(Utils.randomSource, 0.0f, 1.0f) < 0.007f) {
                var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT.get(), 60);
                player.addEffect(newEffect);
            }
        }
        if (!player.hasEffect(ModEffects.PALE_SKIN.get()) && health.getComponent(BLOOD).getConditionValue(BLOOD_LOSS) > 0.4f) {
            player.addEffect(new MobEffectInstance(ModEffects.PALE_SKIN.get(), 100));
        }
        if (!player.hasEffect(ModEffects.HARD_BREATH.get())) {
            if (health.getComponent(BLOOD).getConditionValue(OXYGEN) > 0.2f || health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST))
                player.addEffect(new MobEffectInstance(ModEffects.HARD_BREATH.get(), 100));
        }
        if (!player.hasEffect(MobEffects.BLINDNESS)) {
            if (health.getComponent(BLOOD).getConditionValue(OXYGEN) > 0.5f)
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
        }
        if (!player.hasEffect(MobEffects.WEAKNESS)) {
            var value = health.getComponent(BLOOD).getConditionValue(BLOOD_LOSS);
            if (value > 0.6f)
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100,
                        (int) ((value - 0.6f) / 0.2f)
                ));
        }
    }
}
