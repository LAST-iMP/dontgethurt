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
        var data = livingEntity.getPersistentData();

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
        // If entity is a mob and is down, disable AI to mimic mixin isEffectiveAi behavior
        try {
            if (livingEntity instanceof net.minecraft.world.entity.Mob) {
                net.minecraft.world.entity.Mob mob = (net.minecraft.world.entity.Mob) livingEntity;
                boolean down = HealthCapability.isDown(mob);
                mob.setNoAi(down);
                // clear attack target if it is down and should not be seen as enemy
                try {
                    var brain = mob.getBrain();
                    var maybeTarget = brain.getMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);
                    if (maybeTarget.isPresent()) {
                        var target = (net.minecraft.world.entity.LivingEntity) maybeTarget.get();
                        boolean attackable = !HealthCapability.has(target) || !HealthCapability.isDying(target) || com.lastimp.dgh.common.config.impl.HealthLivingEntityList.canBeSeenWhenLying(target.getType());
                        if (!attackable) {
                            brain.eraseMemory(net.minecraft.world.entity.ai.memory.MemoryModuleType.ATTACK_TARGET);
                        }
                    }
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
        // stop horizontal motion for downed entities
        try {
            if (HealthCapability.isDown(livingEntity) || HealthCapability.isFootLostDown(livingEntity)) {
                var mv = livingEntity.getDeltaMovement();
                livingEntity.setDeltaMovement(0, mv.y, 0);
            }
        } catch (Throwable ignored) {}
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
