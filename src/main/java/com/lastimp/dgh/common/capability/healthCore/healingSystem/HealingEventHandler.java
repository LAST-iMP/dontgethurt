
package com.lastimp.dgh.common.capability.healthCore.healingSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.healthCore.dyingSystem.HealthPhase;
import com.lastimp.dgh.common.config.impl.HealthLivingEntityList;
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

        HealthCapability.getAndApply(livingEntity, h -> {
            h = h.update(livingEntity);
            if (livingEntity instanceof ServerPlayer player && Utils.checkPlayerInvincible(player)) {
                player.setHealth(player.getMaxHealth());
            } else {
                updateLivingHealth(h, livingEntity);
            }
            h.SYNIfDirty(livingEntity);
        });
    }

    private static void updateLivingHealth(HealthCapability health, LivingEntity entity) {
        float maxHealth = health.getHealthWithOuterHealing(entity);

        if (health.getHealthPhase() == HealthPhase.DEAD) {
            DyingHandler.setLivingDead(entity);
        } else if (health.getHealthPhase() == HealthPhase.DYING && !canLieDown(entity)) {
            DyingHandler.setLivingDead(entity);
        } else if (maxHealth > 0) {
            entity.setHealth(maxHealth);
        } else {
            entity.setHealth(0.01f);
        }
    }

    private static boolean canLieDown(LivingEntity entity) {
        var lastDamageSource = entity.getLastDamageSource();
        if (lastDamageSource != null) {
            if (lastDamageSource.is(DamageTypes.FELL_OUT_OF_WORLD) || lastDamageSource.is(DamageTypes.OUTSIDE_BORDER)) return false;
        }
        return HealthLivingEntityList.canEntityLieDown(entity.getType());
    }

    public static void onHealing(LivingEntity livingEntity, float healing) {
        if (livingEntity.level().isClientSide()) return;
        if (!HealthCapability.has(livingEntity)) return;

        float amount = healing * PlatformService.CONFIG.HEALING_FACTOR();
        if (PlatformService.CONFIG.TRADITION_HEALING()) {
            HealingHandler.handleValindaHealing(livingEntity, amount / (livingEntity.getMaxHealth() * PlatformService.CONFIG.BODY_LIFE_FACTOR()));
        } else {
            HealthCapability.getAndApply(livingEntity, h ->
                    h.setOuterHealing(Mth.clamp(h.getOuterHealing() + amount, 0, livingEntity.getMaxHealth()))
            );
        }
    }
}
