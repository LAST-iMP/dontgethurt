package com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class BurnHandler {
    public static boolean handle(DamageSource source, LivingEntity entity, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, BURN, BURN_RES, Component.literal("烧伤"), damageAmount);
    }
}
