
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.livingEntity.DyingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.livingEntity.player.PlayerDyingHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;


@EventBusSubscriber(modid = DontGetHurt.MODID)
public class HealingEventHandler {
    @SubscribeEvent
    public static void onHealthUpdate(EntityTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        if (!HealthCapability.has(livingEntity)) return;

        HealthCapability.getAndSet(livingEntity, h -> {
            h = h.update(livingEntity);
            if (!(livingEntity instanceof ServerPlayer player)) {
                updateLivingHealth(h, livingEntity);
            } else if (!checkTotemDeathProtection(h, player)) {
                updatePlayerHealth(h, player);
            }
            h.SYNIfDirty(livingEntity);
            return h;
        });
    }

    private static void updateLivingHealth(HealthCapability health, LivingEntity entity) {
        float maxHealth = getHealthWithOuterHealing(health, entity);
        if (entity.isDeadOrDying()) {
            DyingHandler.setLivingDead(entity);
        } else if (health.isDown() && maxHealth > 0) {
            entity.setHealth(0.01f);
        } else if (maxHealth > 0) {
            if ((int)maxHealth != (int)entity.getHealth())
                entity.setHealth(maxHealth);
        } else if (maxHealth > -entity.getMaxHealth() && Config.allow_down) {
            entity.setHealth(0.01f);
        } else {
            DyingHandler.setLivingDead(entity);
        }
    }

    private static void updatePlayerHealth(HealthCapability health, ServerPlayer player) {
        float maxHealth = getHealthWithOuterHealing(health, player);
        if (player.isDeadOrDying()) {
            PlayerDyingHandler.setPlayerDead(player);
        } else if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative()) {
            player.setHealth(player.getMaxHealth());
        } else if (health.isDown() && maxHealth > 0) {
            player.setHealth(0.01f);
        } else if (maxHealth > 0) {
            if ((int)maxHealth != (int)player.getHealth())
                player.setHealth(maxHealth);
        } else if (maxHealth > -player.getMaxHealth() && player.getServer().getPlayerList().getPlayers().size() > 1 && Config.allow_down) {
            player.setHealth(0.01f);
        } else {
            PlayerDyingHandler.setPlayerDead(player);
        }
    }

    private static float getHealthWithOuterHealing(HealthCapability health, LivingEntity entity) {
        float maxHealth = entity.getMaxHealth() * health.vitality() + health.outerHealing();
        return Math.min(maxHealth, entity.getMaxHealth());
    }

    @SubscribeEvent
    public static void onHealing(LivingHealEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;

        float amount = event.getAmount() * Config.healing_factor;
        if (Config.tradition_healing) {
            HealingHandler.handleValindaHealing(entity, amount / (entity.getMaxHealth() * Config.body_life_factor));
        } else {
            HealthCapability.getAndSet(entity, (h) -> {
                h.setOuterHealing(Mth.clamp(h.outerHealing() + amount, 0, entity.getMaxHealth()));
                return h;
            });
        }
    }

    private static boolean checkTotemDeathProtection(HealthCapability health, ServerPlayer player) {
        if (getHealthWithOuterHealing(health, player) > 0) return false;

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

        HealingHandler.handleValindaHealing(player, 2);
        player.setHealth(1);
        player.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level().broadcastEntityEvent(player, (byte)35);
        return true;
    }
}