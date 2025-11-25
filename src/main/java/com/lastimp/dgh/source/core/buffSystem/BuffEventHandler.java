package com.lastimp.dgh.source.core.buffSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.Register.ModEffects;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.enums.BodyCondition.WITHDRAW;


@EventBusSubscriber(modid = DontGetHurt.MODID)
public class BuffEventHandler {
    @SubscribeEvent
    public static void onBuffUpdate(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        PlayerHealthCapability health = PlayerHealthCapability.get(player);

        updateStaggerEffects(health, player);
        updateWithdrawEffects(health, player);
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

    private static void updateSymptomsEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (!player.hasEffect(ModEffects.INTENSE_PAIN_EFFECT) && health.intensePain()) {
            var newEffect = new MobEffectInstance(ModEffects.INTENSE_PAIN_EFFECT, 100);
            player.addEffect(newEffect);
        }
    }
}
