
package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.compact.TaZC.BulletsInjuryHandler;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
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
        if (event.getEntity().level().isClientSide) return;
        var livingEntity = event.getEntity();
        if (!HealthCapability.has(livingEntity)) return;

        float damageAmount = event.getNewDamage();
        float absorption = livingEntity.getAbsorptionAmount();
        DamageSource source = event.getSource();

        if (source.is(ModDamageType.FINAL_HEALTH_DAMAGE)) return;
        if (absorption >= damageAmount) return;
        else if (absorption > 0) {
            damageAmount = damageAmount - absorption;
            livingEntity.setAbsorptionAmount(0);
        }

        damageAmount /= livingEntity.getMaxHealth() * Config.body_life_factor;
        if (HealthCapability.isDying(livingEntity)) {
            if (source.getEntity() instanceof Player)
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.PLAYER_RESIST);
            else if (source.getEntity() instanceof LivingEntity)
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.ENTITY_RESIST);
            else
                damageAmount *= HealthLivingEntityList.getEntityDownResist(livingEntity.getType(), HealthLivingEntityList.ENV_RESIST);
        }

        if (source.is(DamageTypeTags.IS_FALL)) {
            handleFalling(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypeTags.BURN_FROM_STEPPING) || source.is(DamageTypes.HOT_FLOOR)) {
            handleHotFloor(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_FIRE)) {
            handleBurning(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_DROWNING)) {
            handleDrowning(event.getSource(), livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            handleExplosion(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.ARROW)) {
            handleArrow(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(ModDamageType.BULLETS)) {
            BulletsInjuryHandler.handleBullet(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.STARVE)) {
            handleStarveDamage(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            handleEntityAttack(event.getSource(), damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC)) {
            handleMagicDamage(event.getSource(), damageAmount, livingEntity, event);
        }  else if (!source.is(DamageTypes.GENERIC_KILL)) {
            handleDefaultDamage(event.getSource(), damageAmount, livingEntity, event);
        }
    }

    public static void handleFalling(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                InternalInjuryHandler.handleBluntTrauma(source, entity, h, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleHotFloor(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                BurnHandler.handle(source, h, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleBurning(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(source, h, (AbstractVisibleBody) h.getComponent(randomComponent), damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleDrowning(DamageSource source, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            Blood blood = (Blood) h.getComponent(BLOOD);
            blood.injury(OXYGEN, BodyCondition.get(OXYGEN).healingSpeed());
            h.addDirectInjury(source.getEntity(), blood.getComponent(), BodyCondition.get(OXYGEN).getComponent(), BodyCondition.get(OXYGEN).healingSpeed());
        });
        event.setNewDamage(0f);
    }

    public static void handleExplosion(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            float[] weight = Utils.getRandom(INJURY_WEIGHT);
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                FollowInjuryHandler.foreignObjectHandler((AbstractVisibleBody)body, h, damageAmount * weight[i], Config.bypass_foreign_prob * 0.6f);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleArrow(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            PassThroughHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0);
    }

    public static void handleEntityAttack(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(Utils.getAttackPart((LivingEntity) source.getEntity(), entity, Config.damage_part_strick_level))));
            OpenWoundHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleMagicDamage(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handleBluntTrauma(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleStarveDamage(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleDefaultDamage(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            InternalInjuryHandler.handle(source, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }
}
