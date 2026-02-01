
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.dyingSystem.DyingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HealingEventHandler {
    @SubscribeEvent
    public static void onHealthUpdate(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        var livingEntity = event.getEntity();
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
}
