package com.lastimp.dgh.source.core.livingEntity.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.livingEntity.DyingHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public class PlayerDyingHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        var player = event.player;

        if (!event.side.isClient()) {
            if (HealthCapability.isDying(player)) {
                if (player.isSleeping()) player.stopSleeping();
                if (player.isFallFlying()) player.stopFallFlying();
                player.stopUsingItem();
            }
        }
    }

    public static void setPlayerDead(Player player) {
        if (player.isSleeping()) player.stopSleeping();
        if (player.isFallFlying()) player.stopFallFlying();
        player.stopUsingItem();
        DyingHandler.setLivingDead(player);
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!HealthCapability.has(player)) return;
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        var key = "dgh_last_death_record";
        HealthCapability.getAndApply(player, health -> HealthCapability.serializeRecord(key, health.directInjury(), persistedTag));
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        HealthCapability.getAndApply(event.getEntity(), newHealth -> {
            var data = player.getPersistentData();
            var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
            var key = "dgh_last_death_record";
            HealthCapability.deserializeRecord(key, newHealth.lastDeathDirectInjury(), persistedTag);
            persistedTag.remove(key);
            data.put(Player.PERSISTED_NBT_TAG, persistedTag);
        });
    }
}
