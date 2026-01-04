package com.lastimp.dgh.source.buffs.debuff;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.config.BlackList;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.COMA;

public class PulseEffect extends MobEffect {
    public PulseEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        this.apply(livingEntity);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int p_19468_) {
        return this.apply(livingEntity);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int p_19455_, int p_19456_) {
        return p_19455_ % 10 == 0;
    }

    @Override
    public void onMobRemoved(LivingEntity livingEntity, int amplifier, Entity.RemovalReason reason) {
        super.onMobRemoved(livingEntity, amplifier, reason);
    }

    private boolean apply(LivingEntity livingEntity) {
        if (BlackList.isEntityBlacklisted(BlackList.PULSE_EFFECT, livingEntity.getType())) return false;
        if (HealthCapability.has(livingEntity)) {
            HealthCapability.getAndSet(livingEntity, h -> {
                Head head = (Head) h.getComponent(BodyComponents.HEAD);
                head.injury(COMA, BodyCondition.get(COMA).healingSpeed() * 1.5f);
            });
        }
        return true;
    }
}
