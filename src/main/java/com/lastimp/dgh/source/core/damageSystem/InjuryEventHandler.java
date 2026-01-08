
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
import net.minecraftforge.event.entity.living.LivingBreatheEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyComponents.VISIBLE_BODIES;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static void onInjury(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;
        var livingEntity = event.getEntity();
        if (!HealthCapability.has(livingEntity)) return;
        DontGetHurt.LOGGER.info(event.getSource().toString() + " " + event.getAmount());

        float damageAmount = event.getAmount();
        float absorption = livingEntity.getAbsorptionAmount();
        DamageSource source = event.getSource();

        if (source.is(ModDamageType.FINAL_HEALTH_DAMAGE)) return;
        if (absorption >= damageAmount) return;
        else if (absorption > 0) {
            damageAmount = damageAmount - absorption;
            livingEntity.setAbsorptionAmount(0);
        }

        damageAmount /= livingEntity.getMaxHealth() * Config.body_life_factor;
        damageAmount /= HealthCapability.isDying(livingEntity) && Config.down_damage_resistance ? 10f : 1f;

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
        } else if (source.is(DamageTypes.STARVE)) {
            handleStarveDamage(damageAmount, livingEntity, event);
        }  else if (!source.is(DamageTypes.GENERIC_KILL)) {
            handleDefaultDamage(damageAmount, livingEntity, event);
        }
    }

    public static void handleFalling(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                InternalInjuryHandler.handleBluntTrauma(entity, (AbstractVisibleBody) leg, damageAmount * weight[i]);
            }
        });
        event.setAmount(0f);
    }

    public static void handleBurning(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            BodyComponents randomComponent = BodyComponents.random();

            BurnHandler.handle(h, h.getComponent(randomComponent), damageAmount);
        });
        event.setAmount(0f);
    }

    public static void handleHotFloor(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1, 1);
            for (int i = 0; i < LEGS.size(); i++) {
                var leg = h.getComponent(LEGS.get(i));
                BurnHandler.handle(h, leg, damageAmount * weight[i]);
            }
        });
        event.setAmount(0f);
    }

    public static void handleDrowning(LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            Blood blood = (Blood) h.getComponent(BLOOD);
            blood.injury(OXYGEN, BodyCondition.get(OXYGEN).healingSpeed() * DELTA);
        });
        event.setAmount(0f);
    }

    public static void handleExplosion(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            float[] weight = Utils.getRandom(1,1.5f,1.5f,1.5f,1.2f,1.2f);
            for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
                var body = h.getComponent(VISIBLE_BODIES.get(i));
                OpenWoundHandler.handleExplosion(entity, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
                InternalInjuryHandler.handleExplosion(entity, (AbstractVisibleBody) body, 0.5f * damageAmount * weight[i]);
            }
        });
        event.setAmount(0f);
    }

    public static void handleEntityAttack(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,1.5f,1.5f,1.5f,1.2f,1.2f)));
            OpenWoundHandler.handleEntityAttack(entity, (AbstractVisibleBody) body, damageAmount);
        });
        event.setAmount(0f);
    }

    public static void handleMagicDamage(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        handleDefaultDamage(damageAmount, entity, event);
        event.setAmount(0f);
    }

    public static void handleStarveDamage(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,1.5f,1.5f,1.5f,1.2f,1.2f)));
            InternalInjuryHandler.handle((AbstractVisibleBody) body, damageAmount);
        });
        event.setAmount(0f);
    }

    public static void handleDefaultDamage(float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndSet(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(1,1.5f,1.5f,1.5f,1.2f,1.2f)));
            InternalInjuryHandler.handleBluntTrauma(entity, (AbstractVisibleBody) body, damageAmount);
        });
        event.setAmount(0f);
    }
}
