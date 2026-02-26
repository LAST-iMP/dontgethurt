
package com.lastimp.dgh.common.capability.healthCore.damageSystem;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.enums.InjuryPart;
import com.lastimp.dgh.common.tags.ModDamageType;
import com.lastimp.dgh.common.config.impl.HealthLivingEntityList;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.BurnHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.InternalInjuryHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.OpenWoundHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.PassThroughHandler;
import com.lastimp.dgh.mixin.entity.LivingEntityAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

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
        InjuryDataSet injuryData = getInjuryData(livingEntity, source);

        float damageAmount = getAbsorbedDamage(livingEntity, source, damage, injuryData);
        if (damageAmount <= 0) return 0;

        damageAmount = getScaledDamage(damageAmount, livingEntity, source);
        var dghHealthDamageEvent = PlatformService.EVENT_HOOK.fireDghHealthDamageEvent(source, damage, damageAmount);
        applyDamage(injuryData, livingEntity, source, dghHealthDamageEvent.newDamage());

        if (livingEntity instanceof ServerPlayer player) {
            player.causeFoodExhaustion(source.getFoodExhaustion());
            player.awardStat(Stats.DAMAGE_TAKEN, Math.round(damageAmount * 10.0F));
        }
        livingEntity.getCombatTracker().recordDamage(source, damageAmount);
        livingEntity.gameEvent(GameEvent.ENTITY_DAMAGE);

        HealthCapability.recordEntityDamage(livingEntity, source);
        return 0;
    }

    public static boolean canInjuryBody(LivingEntity livingEntity, DamageSource source) {
        if (livingEntity.level().isClientSide()) return false;
        if (!HealthCapability.has(livingEntity)) return false;
        return !source.is(ModDamageType.FINAL_HEALTH_DAMAGE) && !source.is(DamageTypes.GENERIC_KILL);
    }

    private static InjuryDataSet getInjuryData(LivingEntity livingEntity, DamageSource source) {
        if (source.is(DamageTypeTags.IS_FALL))
            return handleFalling();
        if (source.is(DamageTypeTags.BURN_FROM_STEPPING) || source.is(DamageTypes.HOT_FLOOR))
            return handleHotFloor();
        if (source.is(DamageTypeTags.IS_FIRE))
            return handleBurning();
        if (source.is(DamageTypeTags.IS_DROWNING))
            return handleDrowning();
        if (source.is(DamageTypeTags.IS_EXPLOSION))
            return handleExplosion();
        if (source.is(DamageTypes.ARROW))
            return handleArrow(source, livingEntity);
        if (source.is(ModDamageType.BULLETS) && source.is(DamageTypeTags.BYPASSES_ARMOR))
            return handleBullet(source, livingEntity);
        if (source.is(DamageTypes.STARVE))
            return handleStarveDamage(source, livingEntity);
        if (source.is(DamageTypes.INDIRECT_MAGIC) || source.is(DamageTypes.MAGIC))
            return handleMagicDamage(source, livingEntity);
        if (source.getEntity() != null && source.getEntity() instanceof LivingEntity)
            return handleEntityAttack(source, livingEntity);
        return handleDefaultDamage(source, livingEntity);
    }

    private static InjuryDataSet handleFalling() {
        return handleFoot(InternalInjuryHandler::handleBluntTrauma);
    }

    private static InjuryDataSet handleHotFloor() {
        return handleFoot(BurnHandler::handle);
    }

    private static InjuryDataSet handleFoot(InjuryDataSet.InjuryHandler<AbstractVisibleBody> handler) {
        float[] weight = Utils.getRandom(1, 1);
        return InjuryDataSet.create(InjuryPart.FEET,
                InjuryDataSet.createData(BodyComponents.LEGS.get(0), weight[0], handler),
                InjuryDataSet.createData(BodyComponents.LEGS.get(1), weight[1], handler)
        );
    }

    private static InjuryDataSet handleBurning() {
        return InjuryDataSet.create(InjuryPart.DEFAULT, InjuryDataSet.createData(BodyComponents.random(), BurnHandler::handle));
    }

    private static InjuryDataSet handleDrowning() {
        return InjuryDataSet.create(InjuryPart.DEFAULT, InjuryDataSet.createData(BLOOD, (source, entity, h, body, damage) ->
                InjuryHandler.handleDirect(entity, h, body, OXYGEN, Component.literal("低血氧"), ConditionAccessor.get(OXYGEN).healingSpeed() * h.bloodOxygenFactor())
        ));
    }

    private static InjuryDataSet handleExplosion() {
        float[] weight = Utils.getRandom(INJURY_WEIGHT());
        var result = new InjuryDataSet.InjuryData<?>[VISIBLE_BODIES.size()];
        for (int i = 0; i < VISIBLE_BODIES.size(); i++) {
            result[i] = InjuryDataSet.createData(VISIBLE_BODIES.get(i), 0.5f * weight[i], (source, entity, h, body, damage) -> {
                OpenWoundHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, damage);
                InternalInjuryHandler.handleExplosion(source, entity, h, (AbstractVisibleBody) body, damage);
                FollowInjuryHandler.foreignObjectHandler((AbstractVisibleBody) body, h, damage * 2, PlatformService.CONFIG.BYPASS_FOREIGN_PROB() * 0.6f);
            });
        }
        return InjuryDataSet.create(InjuryPart.DEFAULT, result);
    }

    private static InjuryDataSet handleArrow(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, PassThroughHandler::handleEntityAttack);
    }

    private static InjuryDataSet handleBullet(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, PassThroughHandler::handleEntityAttack);
    }

    private static InjuryDataSet handleEntityAttack(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, OpenWoundHandler::handleEntityAttack);
    }

    private static InjuryDataSet handleMagicDamage(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, 1.5f, InternalInjuryHandler::handle);
    }

    private static InjuryDataSet handleStarveDamage(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, InternalInjuryHandler::handle);
    }

    private static InjuryDataSet handleDefaultDamage(DamageSource source, LivingEntity entity) {
        return handleSimpleHit(source, entity, InternalInjuryHandler::handle);
    }

    private static InjuryDataSet handleSimpleHit(DamageSource source, LivingEntity target, InjuryDataSet.InjuryHandler<AbstractVisibleBody> handler) {
        return handleSimpleHit(source, target, 1, handler);
    }

    private static InjuryDataSet handleSimpleHit(DamageSource source, LivingEntity target, float factor, InjuryDataSet.InjuryHandler<AbstractVisibleBody> handler) {
        var part = getAttackPart(source.getEntity(), target);
        return InjuryDataSet.create(part, InjuryDataSet.createData(getBodyByPart(part), factor, handler));
    }

    public static BodyComponents getBodyByPart(InjuryPart part) {
        var weight = getAttackPartWeight(part, PlatformService.CONFIG.DAMAGE_PART_STRICK_LEVEL());
        return VISIBLE_BODIES.get(Utils.getRandomIndex(weight));
    }

    public static float[] getAttackPartWeight(InjuryPart part, int strickLevel) {
        if (strickLevel <= 0) return INJURY_WEIGHT();
        switch (part) {
            case InjuryPart.HEAD -> {
                if (strickLevel >= 3) return new float[]{1f,0f,0f,0f,0f,0f};
                if (strickLevel == 2) return new float[]{1f,1f,0f,0f,0f,0f};
                return new float[]{1f,1f,1f,1f,0f,0f};
            } case InjuryPart.BODY -> {
                if (strickLevel >= 3) return new float[]{0f,  1f,1f,1f,0f,0f};
                if (strickLevel == 2) return new float[]{0.9f,1f,1f,1f,0.7f,0.7f};
                return new float[]{1f,1f,1f,1f,0.8f,0.8f};
            } case InjuryPart.FEET -> {
                if (strickLevel >= 3) return new float[]{0f,0f,0f,0f,1f,1f};
                if (strickLevel == 2) return new float[]{0f,0.7f,0.7f,0.7f,1f,1f};
                return new float[]{0f,0.8f,0.8f,0.8f,1f,1f};
            } default -> {
                return INJURY_WEIGHT();
            }
        }
    }

    public static InjuryPart getAttackPart(Entity attacker, LivingEntity target) {
        if (!(attacker instanceof Player player)) {
            return InjuryPart.DEFAULT;
        } else {
            var attackerEye = player.getEyePosition(0.5f);
            var attackerLookAngel = player.getLookAngle();
            var distance_upbound = target.distanceTo(player) + 4;
            var targetBox = target.getBoundingBox();
            var targetHeight = targetBox.maxY - targetBox.minY;

            var hitResult = targetBox.clip(attackerEye, attackerEye.add(attackerLookAngel.multiply(new Vec3(distance_upbound, distance_upbound, distance_upbound))));
            if (hitResult.isEmpty())
                return InjuryPart.DEFAULT;
            var hit = hitResult.get();
            var hitHeight = hit.y - targetBox.minY;

            if (hitHeight > targetHeight * (21d / 29d)) {
                return InjuryPart.HEAD;
            } else if (hitHeight > targetHeight * (11d / 29d)) {
                return InjuryPart.BODY;
            } else {
                return InjuryPart.FEET;
            }
        }
    }

    private static float getAbsorbedDamage(LivingEntity livingEntity, DamageSource damageSource, float damage, InjuryDataSet injuryData) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
            damage = applyEquipment(livingEntity, damageSource, injuryData, damage);
        }

        float absorption = livingEntity.getAbsorptionAmount();
        float absorbed = Math.min(damage, absorption);
        if (absorbed > 0.0F && absorbed < 3.4028235E37F && livingEntity instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbed * 10.0F));
        }
        livingEntity.setAbsorptionAmount(Math.max(0, absorption - absorbed));
        return Math.max(0, damage - absorbed);
    }

    private static float applyEquipment(LivingEntity entity, DamageSource source, InjuryDataSet injuryData, float damage) {
        float valindaArmor = entity.getArmorValue();
        float valindaRoughness = (float) entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        if (!PlatformService.CONFIG.ARMOR_RECALCULATE()) {
            ((LivingEntityAccessor)entity).dgh$doHurtEquipment(source, damage, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.BODY);
            return CombatRules.getDamageAfterAbsorb(entity, damage, source, valindaArmor, valindaRoughness);
        } else {
            int size = injuryData.injuryData().length;
            float totalDamage = 0;
            float[] bodyDamage = new float[size];
            for (int i = 0; i < size; i++) {
                var data = injuryData.injuryData()[i];
                var slots = InjuryDataSet.componentToSlot(data.component());
                ((LivingEntityAccessor)entity).dgh$doHurtEquipment(source, damage * 4 * data.factor() / slots.length, slots);

                float extraArmor = 0, extraRoughness = 0, bodyArmor = 0, bodyRoughness = 0;
                for (var slot : entity.getArmorAndBodyArmorSlots()) {
                    if (slot.getItem() instanceof ArmorItem item) {
                        if (Arrays.stream(slots).anyMatch(equip -> equip == item.getEquipmentSlot())) {
                            bodyArmor += item.getDefense();
                            bodyRoughness += item.getToughness();
                        } else {
                            extraArmor += item.getDefense();
                            bodyRoughness += item.getToughness();
                        }
                    }
                    extraArmor += HealthCapability.getAndApply(entity, h -> (h.getComponent(data.component()) instanceof AbstractVisibleBody visibleBody) ? visibleBody.getArmor() : 0, 0).floatValue();
                    bodyRoughness += HealthCapability.getAndApply(entity, h -> (h.getComponent(data.component()) instanceof AbstractVisibleBody visibleBody) ? visibleBody.getRoughness() : 0, 0).floatValue();
                }
                valindaArmor -= extraArmor + bodyArmor;
                valindaRoughness -= extraRoughness + bodyRoughness;
                bodyDamage[i] = CombatRules.getDamageAfterAbsorb(entity, damage, source, valindaArmor / size + bodyArmor, valindaRoughness / size + bodyRoughness);
                totalDamage += bodyDamage[i];
            }
            for (int i = 0; i < size; i++) {
                var data = injuryData.injuryData()[i];
                data.setFactor(bodyDamage[i] / totalDamage);
            }
            return totalDamage;
        }
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

    public static void applyDamage(final InjuryDataSet injuryData, LivingEntity target, DamageSource source, float scaledDamage) {
        HealthCapability.getAndApply(target, h -> injuryData.handle(target, source, h, scaledDamage));
    }
}
