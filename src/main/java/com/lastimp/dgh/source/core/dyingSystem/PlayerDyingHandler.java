package com.lastimp.dgh.source.core.dyingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class PlayerDyingHandler {
    public static final String HEALTH_PERSISTENT = "health_persistent";
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();

        if (!event.getEntity().level().isClientSide) {
            if (HealthCapability.isDown(player)) {
                if (player.isSleeping()) player.stopSleeping();
                if (player.isFallFlying()) player.stopFallFlying();
                player.stopUsingItem();
            }
        }
    }

    public static void setPlayerDead(ServerPlayer player) {
        if (checkTotemDeathProtection(player)) return;

        if (player.isSleeping()) player.stopSleeping();
        if (player.isFallFlying()) player.stopFallFlying();
        player.stopUsingItem();
        DyingHandler.setLivingDead(player);
    }

    private static boolean checkTotemDeathProtection(ServerPlayer player) {
        ItemStack itemstack = null;
        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack1 = player.getItemInHand(interactionhand);
            DamageSource source = player.level().damageSources().genericKill();
            if (itemstack1.is(Items.TOTEM_OF_UNDYING) && CommonHooks.onLivingUseTotem(player, source, itemstack1, interactionhand)) {
                itemstack = itemstack1.copy();
                itemstack1.shrink(1);
                break;
            }
        }
        if (itemstack == null) return false;

        if (player instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
            CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }

        HealthCapability.getAndApply(player, h -> h.healingAll(true));
        player.setHealth(player.getMaxHealth());
        player.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level().broadcastEntityEvent(player, (byte)35);
        return true;
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player) || !HealthCapability.has(player)) return;
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        HealthCapability.getAndApply(player, health -> {
            health.deserializeNBT(player.registryAccess(), new HealthCapability().serializeNBT(player.registryAccess()));
            persistedTag.put(HealthCapability.HEALTH_RECORD, health.deathSerializeNBT(player.registryAccess()));
        });
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        var player = event.getEntity();
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        HealthCapability.getAndApply(player, newHealth -> newHealth.respawnDeserializeNBT(player.registryAccess(), persistedTag.getCompound(HealthCapability.HEALTH_RECORD)));
        persistedTag.remove(HEALTH_PERSISTENT);
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }
}
