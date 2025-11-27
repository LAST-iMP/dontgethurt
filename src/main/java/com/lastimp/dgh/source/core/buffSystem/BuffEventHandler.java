package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.enums.BodyCondition.WITHDRAW;


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
        AbstractBody[] arms = health.arms();
        int slowDown = (((AbstractArm)arms[0]).available(health) ? 0 : 1) + (((AbstractArm)arms[1]).available(health) ? 0 : 1);
        if (slowDown == 0) return;

        var newEffect = new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                40, slowDown - 1
        );
        if (!player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            player.addEffect(newEffect);
        } else if (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier() >= slowDown) {
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

    private static void updateSymptomsEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (!player.hasEffect(ModEffects.INTENSE_PAIN_EFFECT.get()) && health.intensePain()) {
            var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT.get(), 100);
            player.addEffect(newEffect);
        }
    }
}
