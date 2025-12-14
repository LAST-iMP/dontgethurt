package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.source.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
public class DyingHandler {
    private static boolean showingScreen = false;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();

        if (event.getEntity().level().isClientSide) {
            if (player.getUUID().equals(GuiOpenWrapper.MINECRAFT.get().player.getUUID())){
                if (HealthCapability.isDying(player) && !showingScreen()) {
                    GuiOpenWrapper.openDyingScreen();
                    setShowingScreen(true);
                } else if (!HealthCapability.isDying(player) && showingScreen()){
                    GuiOpenWrapper.closeDyingScreen();
                    setShowingScreen(false);
                }
            }
        } else {
            if (HealthCapability.isDying(player)) {
                if (player.isSleeping()) player.stopSleeping();
                if (player.isFallFlying()) player.stopFallFlying();
                player.stopUsingItem();
                player.setForcedPose(Pose.SWIMMING);
            } else {
                player.setForcedPose(null);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        if (livingEntity instanceof Player) return;
        if (!HealthCapability.has(livingEntity)) return;

        if (!event.getEntity().level().isClientSide) {
            if (HealthCapability.isDying(livingEntity)) {
                if (livingEntity.isSleeping()) livingEntity.stopSleeping();
                livingEntity.stopUsingItem();
                livingEntity.setPose(Pose.SWIMMING);
                if (livingEntity instanceof Mob mob) {
                    mob.setNoAi(true);
                }
            } else {
                livingEntity.setPose(Pose.STANDING);
                if (livingEntity instanceof Mob mob) {
                    mob.setNoAi(false);
                }
            }
        }
    }

    public static void setShowingScreen(boolean showingScreen) {
        DyingHandler.showingScreen = showingScreen;
    }

    public static boolean showingScreen() {
        return DyingHandler.showingScreen;
    }

    public static void setPlayerDead(Player player) {
        if (player.isSleeping()) player.stopSleeping();
        if (player.isFallFlying()) player.stopFallFlying();
        player.stopUsingItem();
        if (player.level().isClientSide)
            GuiOpenWrapper.MINECRAFT.get().setScreen(null);
        setLivingDead(player);
    }

    public static void setLivingDead(LivingEntity entity) {
        entity.hurt(new DamageSource(getKillerDamageType(entity)),entity.getMaxHealth() * 1000);
    }

    public static Holder<DamageType> getKillerDamageType(LivingEntity entity) {
        var damageType = entity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        var health = HealthCapability.get(entity);

        var head = health.getComponent(HEAD);
        if (head.getConditionValue(TRAUMATIC_SHOCK) > 0.4) {
            return damageType.getOrThrow(ModDamageType.SURGERY_DAMAGE);
        }

        var torso = health.getComponent(TORSO);
        if (torso.abnormal(RESPIRATORY_ARREST)) {
            return damageType.getOrThrow(ModDamageType.CANT_BREATH_DAMAGE);
        }

        if (head.getConditionValue(BRAIN_DAMAGE) > 0.8) {
            return damageType.getOrThrow(ModDamageType.BRAIN_DAMAGE);
        }

        var blood = health.getComponent(BLOOD);
        if (blood.getConditionValue(BLOOD_LOSS) > 0.7) {
            return damageType.getOrThrow(ModDamageType.BLEED_DAMAGE);
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
