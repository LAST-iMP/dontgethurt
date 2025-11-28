package com.lastimp.dgh.source.buffs.buff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class KeepLivingEffect extends MobEffect {
    public static String ID_MOVE = "efea5782-b172-4048-b0cb-48622290b4a4";
    public static String ID_ATTACK = "efea5782-b172-4048-b1cb-48622290b4a4";
    public static String ID_BREAK = "efea5782-b172-4048-b2cb-48622290b4a4";

    public KeepLivingEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ID_MOVE,
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                ID_ATTACK,
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
        this.addAttributeModifier(
                Attributes.BLOCK_BREAK_SPEED,
                ID_BREAK,
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
    }
}
