
package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.api.event.EventHooks;
import com.lastimp.dgh.compact.TaZC.BulletsInjuryHandler;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.mixin.LivingEntityAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.subHandler.BurnHandler;
import com.lastimp.dgh.source.core.damageSystem.subHandler.InternalInjuryHandler;
import com.lastimp.dgh.source.core.damageSystem.subHandler.OpenWoundHandler;
import com.lastimp.dgh.source.core.damageSystem.subHandler.PassThroughHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.LEGS;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class InjuryEventHandler {
    public static final float[] INJURY_WEIGHT = {1.2f,1.5f,1.5f,1.5f,1.2f,1.2f};
    @SubscribeEvent
    public static void onBreath(LivingBreatheEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;

        HealthCapability.getAndApply(entity, health -> {
            if (health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST)) {
                event.setCanBreathe(false);
            }
        });
    }

    @SubscribeEvent
    public static void onInjury(LivingDamageEvent.Pre event) {
        if (!canInjuryBody(event)) return;

        float damageAmount = getAbsorbedDamage(event);
        if (damageAmount <= 0) return;

        var livingEntity = event.getEntity();
        DamageSource source = event.getSource();

        damageAmount = getScaledDamage(damageAmount, livingEntity, source);
        var dghHealthDamageEvent = EventHooks.fireDghHealthDamageEvent(source, event.getNewDamage(), damageAmount);
        handle(event, dghHealthDamageEvent.newDamage());

        if (livingEntity instanceof ServerPlayer player) {
            player.causeFoodExhaustion(source.getFoodExhaustion());
            player.awardStat(Stats.DAMAGE_TAKEN, Math.round(damageAmount * 10.0F));
        }
        livingEntity.getCombatTracker().recordDamage(source, damageAmount);
        livingEntity.gameEvent(GameEvent.ENTITY_DAMAGE);
        livingEntity.onDamageTaken(((LivingEntityAccessor)livingEntity).getDamageContainers().peek());

        HealthCapability.recordEntityDamage(livingEntity, source);
    }

    private static boolean canInjuryBody(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return false;
        if (!HealthCapability.has(event.getEntity())) return false;
        return !event.getSource().is(ModDamageType.FINAL_HEALTH_DAMAGE) && !event.getSource().is(DamageTypes.GENERIC_KILL);
    }

    private static float getAbsorbedDamage(LivingDamageEvent.Pre event) {
        float damageAmount = event.getNewDamage();
        var livingEntity = event.getEntity();
        float absorption = livingEntity.getAbsorptionAmount();
        float absorbed = Math.min(damageAmount, absorption);
        if (absorbed > 0.0F && absorbed < 3.4028235E37F && livingEntity instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbed * 10.0F));
        }
        livingEntity.setAbsorptionAmount(Math.max(0, absorption - absorbed));
        return Math.max(0, damageAmount - absorbed);
    }

    private static float getScaledDamage(float damageAmount, LivingEntity livingEntity, DamageSource source) {
        damageAmount /= livingEntity.getMaxHealth() * Config.body_life_factor;
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

    private static void handle(LivingDamageEvent.Pre event, float damageAmount) {
        var source = event.getSource();
        var livingEntity = event.getEntity();
        if (source.is(DamageTypeTags.IS_FALL)) {
            handleFalling(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypeTags.BURN_FROM_STEPPING) || source.is(DamageTypes.HOT_FLOOR)) {
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
            BulletsInjuryHandler.handleBullet(source, damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.STARVE)) {
            handleStarveDamage(source, damageAmount, livingEntity);
        } else if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC)) {
            handleMagicDamage(source, damageAmount, livingEntity);
        } else if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            handleEntityAttack(source, damageAmount, livingEntity);
        } else {
            handleDefaultDamage(source, damageAmount, livingEntity);
        }
        event.setNewDamage(0f);
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

    public static void handleHotFloor(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                BurnHandler.handle(source, h, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
    }

    public static void handleBurning(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(source, h, (AbstractVisibleBody) h.getComponent(randomComponent), damageAmount);
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
            float[] weight = Utils.getRandom(INJURY_WEIGHT);
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                FollowInjuryHandler.foreignObjectHandler((AbstractVisibleBody)body, h, damageAmount * weight[i], Config.bypass_foreign_prob * 0.6f);
            }
        });
    }

    public static void handleArrow(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            PassThroughHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleEntityAttack(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(Utils.getAttackPart((LivingEntity) source.getEntity(), entity, Config.damage_part_strick_level))));
            OpenWoundHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleMagicDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount * 1.5f);
        });
    }

    public static void handleStarveDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
    }

    public static void handleDefaultDamage(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
    }
}
