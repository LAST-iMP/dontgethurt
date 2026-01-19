
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.dyingSystem.DyingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.dyingSystem.PlayerDyingHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;
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
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        if (!HealthCapability.has(livingEntity)) return;

        HealthCapability.getAndApply(livingEntity, h -> {
            h = h.update(livingEntity);
            if (!(livingEntity instanceof ServerPlayer player)) {
                updateLivingHealth(h, livingEntity);
            } else if (!checkTotemDeathProtection(h, player, player.getLastDamageSource())) {
                updatePlayerHealth(h, player);
            }
            h.SYNIfDirty(livingEntity);
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
        } else if (canLieDown(maxHealth, entity)) {
            entity.setHealth(0.01f);
        } else {
            DyingHandler.setLivingDead(entity);
        }
    }

    private static void updatePlayerHealth(HealthCapability health, ServerPlayer player) {
        float maxHealth = getHealthWithOuterHealing(health, player);
        if (player.isDeadOrDying()) {
            PlayerDyingHandler.setPlayerDead(player);
        } else if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative() || player.isSpectator()) {
            player.setHealth(player.getMaxHealth());
        } else if (health.isDown() && maxHealth > 0) {
            player.setHealth(0.01f);
        } else if (maxHealth > 0) {
            if ((int)maxHealth != (int)player.getHealth())
                player.setHealth(maxHealth);
        } else if (canLieDown(maxHealth, player)) {
            player.setHealth(0.01f);
        } else {
            PlayerDyingHandler.setPlayerDead(player);
        }
    }

    private static float getHealthWithOuterHealing(HealthCapability health, LivingEntity entity) {
        float maxHealth = entity.getMaxHealth() * health.vitality() + health.outerHealing();
        return Math.min(maxHealth, entity.getMaxHealth());
    }

    private static boolean canLieDown(float maxHealth, LivingEntity entity) {
        var lastDamageSource = entity.getLastDamageSource();
        if (lastDamageSource != null) {
            if (lastDamageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || lastDamageSource.is(DamageTypes.OUTSIDE_BORDER)) return false;
        }
        return maxHealth > -entity.getMaxHealth() && Config.allow_down;
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
            HealthCapability.getAndApply(entity, h ->
                    h.setOuterHealing(Mth.clamp(h.outerHealing() + amount, 0, entity.getMaxHealth()))
            );
        }
    }

    private static boolean checkTotemDeathProtection(HealthCapability health, ServerPlayer player, DamageSource damageSource) {
        if (getHealthWithOuterHealing(health, player) > 0) return false;
        if (damageSource != null && damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;

        ItemStack itemstack = null;
        DeathProtection deathprotection = null;
        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack1 = player.getItemInHand(interactionhand);
            deathprotection = itemstack1.get(DataComponents.DEATH_PROTECTION);
            if (deathprotection != null && net.neoforged.neoforge.common.CommonHooks.onLivingUseTotem(player, damageSource, itemstack1, interactionhand)) {
                itemstack = itemstack1.copy();
                itemstack1.shrink(1);
                break;
            }
        }

        if (itemstack != null) {
            if (player instanceof ServerPlayer serverplayer) {
                serverplayer.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
                itemstack.causeUseVibration(player, GameEvent.ITEM_INTERACT_FINISH);
            }

            HealingHandler.handleValindaHealing(player, 2);
            player.setHealth(1.0F);
            deathprotection.applyEffects(itemstack, player);
            player.level().broadcastEntityEvent(player, (byte)35);
        }

        return deathprotection != null;
    }
}