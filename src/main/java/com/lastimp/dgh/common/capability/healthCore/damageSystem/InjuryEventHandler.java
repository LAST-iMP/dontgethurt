
package com.lastimp.dgh.common.capability.healthCore.damageSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.compact.TaZC.BulletsInjuryHandler;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModDamageType;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Blood;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.BurnHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.InternalInjuryHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.OpenWoundHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.PassThroughHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

public class InjuryEventHandler {
    public static float[] INJURY_WEIGHT() {
        return new float[]{1.2f, 1.5f, 1.5f, 1.5f, 1.2f, 1.2f};
    }

    public static boolean onBreath(LivingEntity livingEntity) {
        if (livingEntity.level().isClientSide()) return true;
        if (!HealthCapability.has(livingEntity)) return true;

        return HealthCapability.getAndApply(livingEntity,
                health -> !health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST),
                true
        );
    }

    public static float onInjury(LivingEntity livingEntity, DamageSource source, float damage) {
        if (!canInjuryBody(livingEntity, source)) return damage;

        float damageAmount = getAbsorbedDamage(livingEntity, damage);
        if (damageAmount <= 0) return 0 ;

        damageAmount = getScaledDamage(damageAmount, livingEntity, source);
        var dghHealthDamageEvent = PlatformService.EVENT_HOOK.fireDghHealthDamageEvent(source, damage, damageAmount);
        handle(livingEntity, source, dghHealthDamageEvent.newDamage());

        if (livingEntity instanceof ServerPlayer player) {
            player.causeFoodExhaustion(source.getFoodExhaustion());
            player.awardStat(Stats.DAMAGE_TAKEN, Math.round(damageAmount * 10.0F));
        }
        livingEntity.getCombatTracker().recordDamage(source, damageAmount);
        livingEntity.gameEvent(GameEvent.ENTITY_DAMAGE);

        HealthCapability.recordEntityDamage(livingEntity, source);
        return 0;
    }

    private static boolean canInjuryBody(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity.level().isClientSide()) return false;
        if (!HealthCapability.has(livingEntity)) return false;
        return !source.is(ModDamageType.FINAL_HEALTH_DAMAGE) && !source.is(DamageTypes.GENERIC_KILL);
    }

    private static float getAbsorbedDamage(LivingEntity livingEntity, float damage) {
        float absorption = livingEntity.getAbsorptionAmount();
        float absorbed = Math.min(damage, absorption);
        if (absorbed > 0.0F && livingEntity instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbed * 10.0F));
        }
        livingEntity.setAbsorptionAmount(Math.max(0, absorption - absorbed));
        return Math.max(0, damage - absorbed);
    }

    private static float getScaledDamage(float damageAmount, LivingEntity livingEntity, DamageSource source) {
        damageAmount /= livingEntity.getMaxHealth() * PlatformService.CONFIG.BODY_LIFE_FACTOR();
        if (HealthCapability.isDying(livingEntity)) {
            if (source.getEntity() instanceof Player)
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.PLAYER_RESIST);
            else if (source.getEntity() instanceof LivingEntity)
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.ENTITY_RESIST);
            else
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.ENV_RESIST);
        }
        return damageAmount;
    }

    private static void handle(LivingEntity livingEntity, DamageSource source, float damageAmount) {
        if (source.is(DamageTypeTags.IS_FALL)) {
            handleFalling(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypes.HOT_FLOOR)) {
            handleHotFloor(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypeTags.IS_FIRE)) {
            handleBurning(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypeTags.IS_DROWNING)) {
            handleDrowning(source, livingEntity);
        } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            handleExplosion(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypes.ARROW)) {
            handleArrow(source, damageAmount, livingEntity);
        } else if (source.is(ModDamageType.BULLETS)) {
            BulletsInjuryHandler.handleBullet(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypes.STARVE)) {
            handleStarveDamage(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC)) {
            handleMagicDamage(source, damageAmount, livingEntity);
        } else if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            handleEntityAttack(source, damageAmount, livingEntity);
        } else {
            handleDefaultDamage(source, damageAmount, livingEntity);
        }
    }

    public static void handleFalling(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                InternalInjuryHandler.handleBluntTrauma(source, entity, h, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
    }

    public static void handleBurning(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(source, h, (AbstractVisibleBody) h.getComponent(randomComponent), damageAmount);
        });
    }

    public static void handleHotFloor(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                BurnHandler.handle(source, h, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
    }

    public static void handleDrowning(DamageSource source, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            Blood blood = (Blood) h.getComponent(BLOOD);
            InjuryHandler.handleDirect(source.getEntity(), h, blood, OXYGEN, Component.literal("低血氧"), ConditionAccessor.get(OXYGEN).healingSpeed() * h.bloodOxygenFactor());
        });
    }

    public static void handleExplosion(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(INJURY_WEIGHT());
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                FollowInjuryHandler.foreignObjectHandler((AbstractVisibleBody)body, h, damageAmount * weight[i], PlatformService.CONFIG.BYPASS_FOREIGN_PROB() * 0.6f);
            }
        });
    }

    public static void handleArrow(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT())));
            PassThroughHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleEntityAttack(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(Utils.getAttackPart((LivingEntity) source.getEntity(), entity, PlatformService.CONFIG.DAMAGE_PART_STRICK_LEVEL()))));
            OpenWoundHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleMagicDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT())));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount * 1.5f);
        });
    }

    public static void handleStarveDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT())));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleDefaultDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT())));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
    }
}
