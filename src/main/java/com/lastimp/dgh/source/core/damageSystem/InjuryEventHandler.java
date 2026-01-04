
package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModDamageType;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.LEGS;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class InjuryEventHandler {
    @SubscribeEvent
    public static void onBreath(LivingBreatheEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        var entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;

        var health = HealthCapability.get(entity);
        if (health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST)) {
            event.setCanBreathe(false);
        }
    }

    @SubscribeEvent
    public static void onInjury(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        var livingEntity = event.getEntity();
        if (!HealthCapability.has(livingEntity)) return;
        DontGetHurt.LOGGER.info(event.getSource() + " " + event.getNewDamage());

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
        damageAmount /= HealthCapability.isDying(livingEntity)? 10f : 1f;

        if (source.is(DamageTypeTags.IS_FALL)) {
            handleFalling(damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.HOT_FLOOR)) {
            handleHotFloor(damageAmount, livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_FIRE)) {
            handleBurning(damageAmount, livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_DROWNING)) {
            handleDrowning(livingEntity, event);
        } else if (source.is(DamageTypeTags.IS_EXPLOSION)) {
            handleExplosion(damageAmount, livingEntity, event);
        } else if (source.getEntity() != null && source.getEntity() instanceof LivingEntity) {
            handleEntityAttack(damageAmount, livingEntity, event);
        } else if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC)) {
            handleMagicDamage(damageAmount, livingEntity, event);
        } else if (!source.is(DamageTypes.GENERIC_KILL)) {
            handleDefaultDamage(damageAmount, livingEntity, event);
        }
    }

    public static void handleFalling(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                InternalInjuryHandler.handleBluntTrauma(entity, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleHotFloor(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                BurnHandler.handle(h, leg, damageAmount * weight[i]);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleBurning(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(h, h.getComponent(randomComponent), damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleDrowning(LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            Blood blood = (Blood) h.getComponent(BLOOD);
            blood.injury(OXYGEN, BodyCondition.get(OXYGEN).healingSpeed() * DELTA);
        });
        event.setNewDamage(0f);
    }

    public static void handleExplosion(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1,1.5f,1.5f,1.5f,1.2f,1.2f);
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(entity, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(entity, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
            }
        });
        event.setNewDamage(0f);
    }

    public static void handleEntityAttack(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,1.5f,1.5f,1.5f,1.2f,1.2f)));
            OpenWoundHandler.handleEntityAttack(entity, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }

    public static void handleMagicDamage(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        handleDefaultDamage(damageAmount, entity, event);
        event.setNewDamage(0f);
    }

    public static void handleDefaultDamage(float damageAmount, LivingEntity entity, LivingDamageEvent.Pre event) {
        HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,1.5f,1.5f,1.5f,1.2f,1.2f)));
            InternalInjuryHandler.handleBluntTrauma(entity, (AbstractVisibleBody) body, damageAmount);
        });
        event.setNewDamage(0f);
    }
}
