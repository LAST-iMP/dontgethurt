package com.lastimp.dgh.source.core.damageSystem.subHandler;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.FollowInjuryHandler;
import com.lastimp.dgh.source.core.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;

public class PassThroughHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, PASS_THROUGH, OPEN_WOUND_RES, Component.literal("贯穿伤"), damageAmount);
    }

    public static void handleEntityAttack(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        if (!handle(source, health, body, damageAmount)) return;
        float damage = body.getConditionValue(PASS_THROUGH) + body.getConditionHidden(PASS_THROUGH);
        FollowInjuryHandler.fractionHandler(body, health, damage);
        FollowInjuryHandler.pneumothoraxHandler(body, health);
        FollowInjuryHandler.arterialBleedingHandler(body, health);
        FollowInjuryHandler.traumaticAmputationHandler(entity, body, health, damage, Config.baseAmputationThreshold, Config.baseAmputationMaxProb - Config.baseAmputationThreshold, 0.0f, Config.baseAmputationMaxProb);
        FollowInjuryHandler.foreignObjectHandler(body, health, damageAmount, Config.bypass_foreign_prob);
        FollowInjuryHandler.brainDamageHandler(body, health, damageAmount, 0.05f, Config.bypass_brain_damage_prob);
    }
}
