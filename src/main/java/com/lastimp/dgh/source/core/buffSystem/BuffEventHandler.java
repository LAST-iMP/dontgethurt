package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.Register.ModEffects;
import com.lastimp.dgh.source.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.enums.BodyCondition.WITHDRAW;


@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BuffEventHandler {
    @SubscribeEvent
    public static void onBuffUpdate(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        PlayerHealthCapability health = PlayerHealthCapability.get(player);

        updateStaggerEffects(health, player);
        updateArmEffects(health, player);
        updateWithdrawEffects(health, player);
        updateLivingTimeEffects(health, player);
        updateCureEffects(health, player);
        updateSymptomsEffects(health, player);
    }

    private static void updateStaggerEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (health.slowDown() > 0 && !player.hasEffect(ModEffects.STAGGER_EFFECT)) {
            var newEffect = new MobEffectInstance(
                    ModEffects.STAGGER_EFFECT,
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
        if (state.getValue() > 0.2f && !player.hasEffect(ModEffects.CRAVING_EFFECT)) {
            player.addEffect(new MobEffectInstance(ModEffects.CRAVING_EFFECT, 100));
        }
        if (state.getValue() > 0.3f && !player.hasEffect(ModEffects.SWEATING_EFFECT)) {
            player.addEffect(new MobEffectInstance(ModEffects.SWEATING_EFFECT, 100));
        }
        if (state.getValue() > 0.4f && !player.hasEffect(MobEffects.CONFUSION)) {
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100));
        }
    }

    private static void updateLivingTimeEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (player.hasEffect(ModEffects.KEEP_LIVING_EFFECT)) return;
        int amp = (int) Math.sqrt((double) health.livingTick() / 1000);
        amp = Math.min(amp, 40);
        if (amp < 1) return;
        var newEffect = new MobEffectInstance(
                ModEffects.KEEP_LIVING_EFFECT,
                100, amp - 1,
                false, false, true
        );
        player.addEffect(newEffect);
    }

    private static void updateCureEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (player.hasEffect(ModEffects.CURE_EFFECT)) return;
        if (health.playerVitality() < 0.999f) return;
        if (health.almostDead() < 0.2f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 2));
        } else if (health.almostDead() < 0.5f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 1));
        } else if (health.almostDead() < 0.8f) {
            player.addEffect(new MobEffectInstance(ModEffects.CURE_EFFECT, 2400, 0));
        } else {
            return;
        }
        health.resetAlmostDead();
    }

    private static void updateSymptomsEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (!player.hasEffect(ModEffects.INTENSE_PAIN_EFFECT) && health.intensePain()) {
            var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT, 100);
            player.addEffect(newEffect);
        }
    }
}
