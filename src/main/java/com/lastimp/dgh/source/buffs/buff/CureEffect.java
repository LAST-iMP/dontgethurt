package com.lastimp.dgh.source.buffs.buff;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class CureEffect extends MobEffect {
    public static String ID_DAMAGE = "efaa5782-b172-4048-b0cb-48622290b4a4";
    public static String ID_ABSORB = "efaa5782-b123-4048-b0cb-48622290b4a4";

    public CureEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
        this.setBlendDuration(0);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ID_DAMAGE,
                2f,
                AttributeModifier.Operation.ADD_VALUE
        );
        this.addAttributeModifier(
                Attributes.MAX_ABSORPTION,
                ID_ABSORB,
                2f,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getAbsorptionAmount() < amplifier * 2 + 2)
            livingEntity.setAbsorptionAmount(amplifier * 2 + 2);
        return true;
    }
}
