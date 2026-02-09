package com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractExtremities;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.FollowInjuryHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public abstract class InternalInjuryHandler {
    public static boolean handle(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, INTERNAL_INJURY, INTERNAL_RES, Component.literal("内伤"), damageAmount);
    }

    public static void handleBluntTrauma(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, entity, health, body, damageAmount)) return;
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD() + 0.2f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB() - PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD() - 0.2f, 0.0f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB());
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, entity, health, body, damageAmount)) return;
        float damage = body.getConditionValue(INTERNAL_INJURY) + body.getConditionHidden(INTERNAL_INJURY);
        if (body instanceof AbstractExtremities extremities)
            FollowInjuryHandler.dislocationHandler(extremities, health, damage, PlatformService.CONFIG.BASE_DISLOCATION_THRESHOLD(), 1, 0.15f, 0.36f, 0);
        FollowInjuryHandler.fractionHandler(body, health, damage, PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD(), 0.9f - PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD(), 0.0f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD() + 0.2f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB() - PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD() - 0.2f, 0.0f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB());
    }
}
