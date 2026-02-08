package com.lastimp.dgh.compact.TaZC;

import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryEventHandler;
import com.lastimp.dgh.common.capability.healthCore.damageSystem.subHandler.PassThroughHandler;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.common.enums.BodyComponents.VISIBLE_BODIES;
import static com.lastimp.dgh.common.capability.healthCore.damageSystem.InjuryEventHandler.INJURY_WEIGHT;

public class BulletsInjuryHandler {
    public static void handleBullet(DamageSource source, float damageAmount, LivingEntity entity) {
        boolean isByPass = source.is(DamageTypeTags.BYPASSES_ARMOR);
        if (!isByPass) {
            InjuryEventHandler.handleEntityAttack(source, damageAmount, entity);
        } else {
            handleBulletByPass(source, damageAmount, entity);
        }
    }

    public static void handleBulletByPass(DamageSource source, float damageAmount, LivingEntity entity) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT())));
            PassThroughHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
    }
}
