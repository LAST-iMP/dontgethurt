package com.lastimp.dgh.source.core.damageSystem.subHandler;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.FollowInjuryHandler;
import com.lastimp.dgh.source.core.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class OpenWoundHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, OPEN_WOUND, OPEN_WOUND_RES, Component.literal("撕裂伤"), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }

    public static void handleExplosion(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(OPEN_WOUND) + body.getConditionHidden(OPEN_WOUND);
        FollowInjuryHandler.fractionHandler(body, health, damage, Config.baseFractureThreshold, 0.9f - Config.baseFractureThreshold, 0.1f, 1.0f, 0);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
    }
}
