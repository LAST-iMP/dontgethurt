package com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.FollowInjuryHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class OpenWoundHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source, health, body, OPEN_WOUND, OPEN_WOUND_RES, Component.literal("撕裂伤"), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB() - PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), 0.0f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB());
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage, PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD(), 0.9f - PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD(), 0.1f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB() - PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), 0.0f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB());
    }
}
