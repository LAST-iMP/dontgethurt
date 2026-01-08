package com.lastimp.dgh.source.core.livingEntity;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public class DyingHandler {
    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) return;
        var livingEntity = event.getEntity();
        if (livingEntity instanceof Player) return;
        if (!HealthCapability.has(livingEntity)) return;

        if (!event.getEntity().level().isClientSide) {
            if (HealthCapability.isDying(livingEntity)) {
                if (livingEntity.isSleeping()) livingEntity.stopSleeping();
                livingEntity.stopUsingItem();
            }
        }
    }

    public static void setLivingDead(LivingEntity entity) {
        var health = HealthCapability.get(entity);
        if (!health.oxygenMask().getStackInSlot(0).isEmpty()) {
            Utils.drop(health.oxygenMask().getStackInSlot(0), entity);
            health.oxygenMask().setStackInSlot(0, ItemStack.EMPTY);
        }
        if (!health.autoPulse().getStackInSlot(0).isEmpty()) {
            Utils.drop(health.autoPulse().getStackInSlot(0), entity);
            health.autoPulse().setStackInSlot(0, ItemStack.EMPTY);
        }
        Entity source = null;
        Entity directSource = null;
        var lastDamageSource = entity.getLastDamageSource();
        if (lastDamageSource != null) {
            source = lastDamageSource.getEntity();
            directSource = lastDamageSource.getDirectEntity();
        }
        entity.hurt(new DamageSource(getKillerDamageType(entity), source, directSource),entity.getMaxHealth() * 10000);
    }

    public static Holder<DamageType> getKillerDamageType(LivingEntity entity) {
        var damageType = entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        var health = HealthCapability.get(entity);

        var head = health.getComponent(HEAD);
        if (head.getConditionValue(TRAUMATIC_SHOCK) > 0.4) {
            return damageType.getOrThrow(ModDamageType.SURGERY_DAMAGE);
        }

        var torso = health.getComponent(TORSO);
        if (torso.abnormal(RESPIRATORY_ARREST)) {
            return damageType.getOrThrow(ModDamageType.CANT_BREATH_DAMAGE);
        }

        var blood = health.getComponent(BLOOD);
        if (blood.getConditionValue(BLOOD_LOSS) > 0.7) {
            return damageType.getOrThrow(ModDamageType.BLEED_DAMAGE);
        }

        if (head.getConditionValue(BRAIN_DAMAGE) > 0.9) {
            return damageType.getOrThrow(ModDamageType.BRAIN_DAMAGE);
        }

        var left_arm = health.getComponent(LEFT_ARM);
        var left_leg = health.getComponent(LEFT_LEG);
        var right_arm = health.getComponent(RIGHT_ARM);
        var right_leg = health.getComponent(RIGHT_LEG);
        float internal_injury = left_arm.getConditionValue(INTERNAL_INJURY) + left_leg.getConditionValue(INTERNAL_INJURY) +
                right_arm.getConditionValue(INTERNAL_INJURY) + right_leg.getConditionValue(INTERNAL_INJURY);
        float open_wound = left_arm.getConditionValue(OPEN_WOUND) + left_leg.getConditionValue(OPEN_WOUND) +
                right_arm.getConditionValue(OPEN_WOUND) + right_leg.getConditionValue(OPEN_WOUND) +
                left_arm.getConditionHidden(OPEN_WOUND) + left_leg.getConditionHidden(OPEN_WOUND) +
                right_arm.getConditionHidden(OPEN_WOUND) + right_leg.getConditionHidden(OPEN_WOUND);
        float burn = left_arm.getConditionValue(BURN) + left_leg.getConditionValue(BURN) +
                right_arm.getConditionValue(BURN) + right_leg.getConditionValue(BURN) +
                left_arm.getConditionHidden(BURN) + left_leg.getConditionHidden(BURN) +
                right_arm.getConditionHidden(BURN) + right_leg.getConditionHidden(BURN);
        if (burn > open_wound && burn > internal_injury) {
            return damageType.getOrThrow(ModDamageType.BURN_DAMAGE);
        }
        if (open_wound > internal_injury) {
            return damageType.getOrThrow(ModDamageType.OPEN_WOUND_DAMAGE);
        }
        return damageType.getOrThrow(ModDamageType.INTERNAL_INJURY_DAMAGE);
    }
}
