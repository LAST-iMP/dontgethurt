package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.Register.ModEffects;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class HealingEventHandler {

    @SubscribeEvent
    public static void onHealingUpdate(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        PlayerHealthCapability.getAndSet(player, health -> {
            health = health.update(player);
            return health;
        });
    }

    @SubscribeEvent
    public static void onHealingUpdate(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        PlayerHealthCapability health = PlayerHealthCapability.get(player);

        updatePlayerHealth(health, player);
        updateEffects(health, player);
    }

    private static void updatePlayerHealth(PlayerHealthCapability health, ServerPlayer player) {
        float maxHealth = player.getMaxHealth() * health.playerVitality();

        if ((int)maxHealth != (int)player.getHealth() && player.getHealth() > 0)
            player.setHealth(maxHealth);
        if (maxHealth <= 0)
            player.setHealth(0);
    }

    private static void updateEffects(PlayerHealthCapability health, ServerPlayer player) {
        if (health.slowDown() > 0 && !player.hasEffect(ModEffects.STAGGER_EFFECT)) {
            var newEffect = new MobEffectInstance(
                    ModEffects.STAGGER_EFFECT,
                    10, health.slowDown() - 1
            );
            player.addEffect(newEffect);
        }
    }
}
