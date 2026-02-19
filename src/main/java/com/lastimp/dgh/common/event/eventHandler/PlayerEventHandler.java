package com.lastimp.dgh.common.event.eventHandler;

import com.lastimp.dgh.common.config.ModConfigs;
import com.lastimp.dgh.common.item.bases.AbstractHealingItem;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public class PlayerEventHandler {
    private static final String WINNING = HealthCapability.HEALTH_RECORD + "_win";
    public static void logIn(Player player) {
        if (player.level().isClientSide) return;

        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        var key = "dgh_new_player";
        if (!persistedTag.getBoolean(key)) {
            player.getInventory().add(new ItemStack(ModItems.HEALTH_CARE_BAG.get()));
            player.getInventory().add(new ItemStack(ModItems.BANDAGE.get(), 8));
            player.getInventory().add(new ItemStack(ModItems.MORPHINE.get(), 2));
            persistedTag.putBoolean(key, true);
            data.put(Player.PERSISTED_NBT_TAG, persistedTag);
        }
        ModConfigs.synToPlayer((ServerPlayer) player);
    }

    public static void logOut(Player player) {
        if (player.level().isClientSide) return;

        GameRules rules = player.level().getGameRules();
        if(!player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(true, player.level().getServer());
        }
    }

    public static InteractionResult onPlayerInteractEntity(Player player, Entity target, InteractionHand hand) {
        if (!(target instanceof LivingEntity livingEntity)) return InteractionResult.PASS;
        if (!HealthCapability.isDown(livingEntity)) return InteractionResult.PASS;

        var item = player.getMainHandItem();
        if (item.getItem() instanceof AbstractHealingItem healingItem) {
            healingItem.interactLivingEntity(item, player, livingEntity, hand);
        }
        return InteractionResult.CONSUME;
    }

    public static void onPlayerTravelDimension(ServerPlayer player, ResourceKey<Level> target) {
        if (!HealthCapability.has(player)) return;

        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        ResourceKey<Level> resourcekey = player.serverLevel().dimension();
        if (resourcekey == Level.END && target == Level.OVERWORLD) {
            HealthCapability.getAndApply(player, h ->
                    persistedTag.put(WINNING, h.serialize())
            );
        } else if (persistedTag.contains(WINNING)) {
            persistedTag.remove(WINNING);
        }
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }

    public static void onPlayerRespawn(Player player) {
        if (player.level().isClientSide()) return;
        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        var key = persistedTag.contains(WINNING) ? WINNING : HealthCapability.HEALTH_RECORD;
        HealthCapability.getAndApply(player, newHealth -> {
            if (persistedTag.contains(key)) {
                newHealth.deserialize(persistedTag.getCompound(key));
            } else {
                newHealth.respawnDeserializeNBT(persistedTag.getCompound(key));
            }
            persistedTag.remove(key);
        });
        data.put(Player.PERSISTED_NBT_TAG, persistedTag);
    }
}