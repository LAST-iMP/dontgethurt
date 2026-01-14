package com.lastimp.dgh.source.core.dyingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class PlayerDyingHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();

        if (!event.getEntity().level().isClientSide) {
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
        HealthCapability.getAndApply(player, health -> HealthCapability.serializeRecord(key, health.directInjury(), persistedTag, player.registryAccess()));
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        HealthCapability.getAndApply(event.getEntity(), newHealth -> {
            var data = player.getPersistentData();
            var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
            var key = "dgh_last_death_record";
            HealthCapability.deserializeRecord(key, newHealth.lastDeathDirectInjury(), persistedTag, player.registryAccess());
            persistedTag.remove(key);
            data.put(Player.PERSISTED_NBT_TAG, persistedTag);
        });
    }
}
