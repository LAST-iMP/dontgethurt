package com.lastimp.dgh.source.buffs.debuff;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.config.BlackList;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.COMA;

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

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(livingEntity, attributeMap, amplifier);
        if (livingEntity instanceof Mob mob) {
            mob.setNoAi(false);
        }
    }

    private void apply(LivingEntity livingEntity) {
        if (BlackList.isEntityBlacklisted(BlackList.PULSE_EFFECT, livingEntity.getType())) return;
        if (HealthCapability.has(livingEntity)) {
            HealthCapability.getAndSet(livingEntity, h -> {
                Head head = (Head) h.getComponent(BodyComponents.HEAD);
                head.injury(COMA, BodyCondition.get(COMA).healingSpeed() * 1.5f);
            });
        } else if (livingEntity instanceof Mob mob) {
            mob.setNoAi(true);
            mob.targetSelector.removeAllGoals(e -> true);
            mob.goalSelector.removeAllGoals(e -> true);
        }
    }
}
