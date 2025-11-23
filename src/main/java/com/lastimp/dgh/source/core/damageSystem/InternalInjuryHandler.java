package com.lastimp.dgh.source.core.damageSystem;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class InternalInjuryHandler {
//    public static PlayerHealthCapability handle(PlayerHealthCapability health, BodyComponents component, float damageAmount) {
//        var body = health.getComponent(component);
//        body.injury(INTERNAL_INJURY, damageAmount / INTERNAL_INJURY.factor);
//        return health;
//    }
//
//    public static PlayerHealthCapability handleWithBones(PlayerHealthCapability health, BodyComponents component, float damageAmount) {
//        handle(health, component, damageAmount);
//
//        var body = health.getComponent(component);
//        var state = body.getCondition(INTERNAL_INJURY);
//        var totalDamage = state.getValue() + state.getHiddenValue();
//        if (body instanceof AbstractExtremities extremities) {
//            if (Utils.randomCheck(damageAmount - 2)) {
//                extremities.setConditionValue(DISLOCATION, DISLOCATION.maxValue);
//            }
//            if (Utils.randomCheck(totalDamage - 2)) {
//
//            }
//        }
//        return health;
//    }
}
