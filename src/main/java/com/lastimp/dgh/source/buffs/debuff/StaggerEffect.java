package com.lastimp.dgh.source.buffs.debuff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.*;

import java.util.UUID;

public class StaggerEffect extends MobEffect {
    public static String ID = "efea5782-b172-4048-b0cb-48622290b4b3";

    public StaggerEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);

        AttributeInstance instance = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (instance == null) return;

        double amount = -0.05 * (amplifier + 1);
        AttributeModifier modifier = new AttributeModifier(
                UUID.fromString(ID),
                "stagger_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
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
