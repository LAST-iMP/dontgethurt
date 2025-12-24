package com.lastimp.dgh.source.buffs.buff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AdrenalineEffect extends MobEffect {
    public static String ID = "D213A07B-BF85-48AA-8BC6-8CEC710006C1";
    public AdrenalineEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ID,
                0.2f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
