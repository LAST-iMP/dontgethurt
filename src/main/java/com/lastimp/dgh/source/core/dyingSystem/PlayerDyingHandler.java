package com.lastimp.dgh.source.core.dyingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
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

        if (!event.getEntity().level().isClientSide()) {
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
        var key = "dgh_last_death_record";
        var data = player.getPersistentData();
        var persistedTag = data.getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
        TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        HealthCapability.getAndApply(player, health -> HealthCapability.serializeRecord(key, health.directInjury(), output));
        persistedTag.put(key, output.buildResult());
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        HealthCapability.getAndApply(event.getEntity(), newHealth -> {
            var data = player.getPersistentData();
            var persistedTag = data.getCompoundOrEmpty(Player.PERSISTED_NBT_TAG);
            var key = "dgh_last_death_record";
            var input = TagValueInput.create(ProblemReporter.DISCARDING, player.registryAccess(), persistedTag.getCompoundOrEmpty(key));
            HealthCapability.deserializeRecord(key, newHealth.lastDeathDirectInjury(), input);
            persistedTag.remove(key);
            data.put(Player.PERSISTED_NBT_TAG, persistedTag);
        });
    }
}
