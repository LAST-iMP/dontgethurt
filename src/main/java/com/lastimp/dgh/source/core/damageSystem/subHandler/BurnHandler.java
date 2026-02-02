package com.lastimp.dgh.source.core.damageSystem.subHandler;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.InjuryHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class BurnHandler {
    public static boolean handle(DamageSource source, HealthCapability health, AbstractVisibleBody body, float damageAmount) {
        return InjuryHandler.handle(source.getEntity(), health, body, BURN, BURN_RES, Component.literal("烧伤"), damageAmount);
    }
}
