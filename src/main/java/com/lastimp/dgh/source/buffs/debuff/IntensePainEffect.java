package com.lastimp.dgh.source.buffs.debuff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class IntensePainEffect extends MobEffect {
    public static String ID = "efea5782-b172-4048-b12b-48622290b4b3";

    public IntensePainEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ID,
                -0.5f,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
    }
}
