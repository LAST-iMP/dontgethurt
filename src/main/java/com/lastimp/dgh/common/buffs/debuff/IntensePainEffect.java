package com.lastimp.dgh.common.buffs.debuff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class IntensePainEffect extends MobEffect {
    public static String ID = "efea5782-b172-4048-b12b-48622290b4b3";

    public IntensePainEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);

        AttributeInstance instance = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (instance == null) return;

        double amount = -0.5 * (amplifier + 1);
        AttributeModifier modifier = new AttributeModifier(
                UUID.fromString(ID),
                "intense_pain_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_BASE
        );

        instance.removeModifier(UUID.fromString(ID));
        instance.addTransientModifier(modifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        AttributeInstance instance = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (instance == null) return;

        instance.removeModifier(UUID.fromString(ID));
    }
}
