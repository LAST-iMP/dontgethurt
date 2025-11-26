
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HealingEventHandler {
    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        GameRules rules = event.getEntity().level().getGameRules();
        if(event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.getEntity().level().getServer());
        }
    }

    @SubscribeEvent
    public static void onHealingUpdate(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;

        ServerPlayer player = (ServerPlayer) event.player;
        PlayerHealthCapability health = PlayerHealthCapability.getAndSet(player, h -> {
            h = h.update(player);
            return h;
        });

        updatePlayerHealth(health, player);
    }

    private static void updatePlayerHealth(PlayerHealthCapability health, ServerPlayer player) {
        float maxHealth = player.getMaxHealth() * health.playerVitality();

        if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative())
            maxHealth = 1.0f;
        if ((int)maxHealth != (int)player.getHealth() && player.getHealth() > 0)
            player.setHealth(maxHealth);
        if (maxHealth <= 0)
            player.setHealth(0);
    }
}
