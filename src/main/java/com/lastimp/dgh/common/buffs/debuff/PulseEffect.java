package com.lastimp.dgh.common.buffs.debuff;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Head;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.COMA;

public class PulseEffect extends MobEffect {
    public PulseEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);
        this.apply(livingEntity);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int p_19468_) {
        this.apply(livingEntity);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return p_19455_ % 10 == 0;
    }

    private void apply(LivingEntity livingEntity) {
        if (HealthCapability.has(livingEntity)) {
            HealthCapability.getAndApply(livingEntity, h -> {
                Head head = (Head) h.getComponent(BodyComponents.HEAD);
                head.injury(COMA, ConditionAccessor.get(COMA).healingSpeed() * 1.5f);
            });
        }
    }
}
