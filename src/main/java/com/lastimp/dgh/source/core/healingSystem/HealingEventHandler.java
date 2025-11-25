
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HealingEventHandler {

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

        if ((int)maxHealth != (int)player.getHealth() && player.getHealth() > 0)
            player.setHealth(maxHealth);
        if (maxHealth <= 0)
            player.setHealth(0);
    }
}
