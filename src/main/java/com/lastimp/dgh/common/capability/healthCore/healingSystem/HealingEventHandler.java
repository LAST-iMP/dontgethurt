
package com.lastimp.dgh.common.capability.healthCore.healingSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.healthCore.dyingSystem.DyingHandler;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HealingEventHandler {
    public static void onHealthUpdate(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide()) return;
        if (livingEntity.isDeadOrDying()) return;
        if (!HealthCapability.has(livingEntity)) return;

        boolean isDying = HealthCapability.isDying(livingEntity);
        boolean isDown = HealthCapability.isDown(livingEntity);
        HealthCapability.getAndApply(livingEntity, h -> {
            h = h.update(livingEntity);
            if (livingEntity instanceof ServerPlayer player && Utils.checkPlayerInvincible(player)) {
                player.setHealth(player.getMaxHealth());
            } else {
                updateLivingHealth(h, livingEntity);
            }
            h.SYNIfDirty(livingEntity);
        });
        var name = livingEntity instanceof Player ? livingEntity.getScoreboardName() : livingEntity.getName().getString();
        if (!isDying && HealthCapability.isDying(livingEntity) && canLieDown(-1, livingEntity)) {
            Utils.broadcastMessageToTeam(livingEntity, livingEntity.getCombatTracker().getDeathMessage());
            Utils.broadcastMessageToTeam(livingEntity, Component.literal(name + "重伤濒死！"));
        } else if (!isDown && HealthCapability.isDown(livingEntity)) {
            Utils.broadcastMessageToTeam(livingEntity, Component.literal(name + "晕倒了！"));
        }
    }

    private static void updateLivingHealth(HealthCapability health, LivingEntity entity) {
        float maxHealth = getHealthWithOuterHealing(health, entity);
        if (maxHealth > 0) {
            if ((int)maxHealth != (int)entity.getHealth())
                entity.setHealth(maxHealth);
        } else if (canLieDown(maxHealth, entity)) {
            entity.setHealth(0.01f);
        } else if (!entity.isDeadOrDying()){
            DyingHandler.setLivingDead(entity);
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
        return maxHealth > -entity.getMaxHealth() && HealthLivingEntityList.canEntityLieDown(entity.getType());
    }

    public static void onHealing(LivingEntity livingEntity, float healing) {
        if (livingEntity.level().isClientSide()) return;
        if (!HealthCapability.has(livingEntity)) return;

        float amount = healing * PlatformService.CONFIG.HEALING_FACTOR();
        if (PlatformService.CONFIG.TRADITION_HEALING()) {
            HealingHandler.handleValindaHealing(livingEntity, amount / (livingEntity.getMaxHealth() * PlatformService.CONFIG.BODY_LIFE_FACTOR()));
        } else {
            HealthCapability.getAndApply(livingEntity, h ->
                    h.setOuterHealing(Mth.clamp(h.outerHealing() + amount, 0, livingEntity.getMaxHealth()))
            );
        }
    }
}
