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

public class PassThroughHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source, health, body, PASS_THROUGH, OPEN_WOUND_RES, Component.literal("贯穿伤"), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(PASS_THROUGH) + body.getConditionHidden(PASS_THROUGH);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB() - PlatformService.CONFIG.BASE_AMPUTATION_THRESHOLD(), 0.0f, PlatformService.CONFIG.BASE_AMPUTATION_MAX_PROB());
        FollowInjuryHandler.foreignObjectHandler(body, health, damageAmount, PlatformService.CONFIG.BYPASS_FOREIGN_PROB());
        FollowInjuryHandler.brainDamageHandler(body, health, damageAmount, 0.05f, PlatformService.CONFIG.BYPASS_BRAIN_DAMAGE_PROB());
    }
}
