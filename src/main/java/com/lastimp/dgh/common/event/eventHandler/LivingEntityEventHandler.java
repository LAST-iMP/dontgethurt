package com.lastimp.dgh.common.event.eventHandler;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.buffSystem.BuffEventHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryEventHandler;
import com.lastimp.dgh.common.capability.healthCore.dyingSystem.DyingHandler;
import com.lastimp.dgh.common.capability.healthCore.healingSystem.HealingEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class LivingEntityEventHandler {
    public static void addOrgan(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide()) return;
        var key = "dgh_version";
        var value = "version_1.3.0";
        var data = livingEntity.save().getPersistentData();

        if (livingEntity instanceof Player player) {
            var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
            if (!persistedTag.getString(key).equals(value)) {
                HealthCapability.getAndApply(player, h -> h.addOriginOrganFully(player));
                persistedTag.putString(key, value);
                data.put(Player.PERSISTED_NBT_TAG, persistedTag);
            }
        } else {
            if (!data.getString(key).equals(value)) {
                data.putString(key, value);
                HealthCapability.getAndApply(livingEntity, h -> h.addOriginOrganFully(livingEntity));
                data.putString(key, value);
            }
        }
    }

    public static void tickPre(LivingEntity livingEntity) {
        HealingEventHandler.onHealthUpdate(livingEntity);
        BuffEventHandler.onBuffUpdate(livingEntity);
        DyingHandler.checkIfDown(livingEntity);
    }

    public static boolean onBreath(LivingEntity livingEntity) {
        return InjuryEventHandler.onBreath(livingEntity);
    }

    public static float onInjury(LivingEntity livingEntity, DamageSource source, float damage) {
        return InjuryEventHandler.onInjury(livingEntity, source, damage);
    }

    public static void onHealing(LivingEntity livingEntity, float healing) {
        HealingEventHandler.onHealing(livingEntity, healing);
    }

    public static void onLivingDeath(LivingEntity livingEntity) {
        DyingHandler.onLivingDeath(livingEntity);
    }
}
