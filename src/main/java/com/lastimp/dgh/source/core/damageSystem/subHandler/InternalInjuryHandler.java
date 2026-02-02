package com.lastimp.dgh.source.core.damageSystem.subHandler;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractExtremities;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.FollowInjuryHandler;
import com.lastimp.dgh.source.core.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, INTERNAL_INJURY, INTERNAL_RES, Component.literal("内伤"), damageAmount);
    }

    public static void handleBluntTrauma(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage, Config.baseDislocationThreshold, 1, 0.15f, 0.36f, 0);
        FollowInjuryHandler.fractionHandler(body, health, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold + 0.2f, Config.baseAmputationMaxProb - Config.baseAmputationThreshold - 0.2f, 0.0f, Config.baseAmputationMaxProb);
    }
}
