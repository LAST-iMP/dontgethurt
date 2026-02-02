package com.lastimp.dgh.compact.TaZC;

import com.lastimp.dgh.source.core.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.damageSystem.InjuryEventHandler;
import com.lastimp.dgh.source.core.damageSystem.subHandler.PassThroughHandler;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import static com.lastimp.dgh.api.enums.BodyComponents.VISIBLE_BODIES;
import static com.lastimp.dgh.source.core.damageSystem.InjuryEventHandler.INJURY_WEIGHT;

public class BulletsInjuryHandler {
    public static void handleBullet(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        boolean isByPass = event.getSource().is(DamageTypeTags.BYPASSES_ARMOR);
        if (!isByPass) {
            InjuryEventHandler.handleEntityAttack(source, damageAmount, entity, event);
        } else {
            handleBulletByPass(source, damageAmount, entity, event);
        }
    }

    public static void handleBulletByPass(DamageSource source, float damageAmount, LivingEntity entity, LivingDamageEvent event) {
        HealthCapability.getAndApply(entity, h -> {
            var body = h.getComponent(VISIBLE_BODIES.get(Utils.getRandomIndex(INJURY_WEIGHT)));
            PassThroughHandler.handleEntityAttack(source, entity, h, (AbstractVisibleBody) body, damageAmount);
        });
        event.setAmount(0);
    }
}
