package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class HealingEventHandler {
    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        GameRules rules = event.getEntity().level().getGameRules();
        if(event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.getEntity().level().getServer());
        }
    }

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
    }

    private static void updatePlayerHealth(PlayerHealthCapability health, ServerPlayer player) {
        float maxHealth = player.getMaxHealth() * health.playerVitality();

        if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative())
            maxHealth = player.getMaxHealth();
        if ((int)maxHealth != (int)player.getHealth() && player.getHealth() > 0)
            player.setHealth(maxHealth);
        if (maxHealth <= 0)
            player.setHealth(0);
    }
}
