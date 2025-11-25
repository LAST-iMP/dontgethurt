package com.lastimp.dgh.source.buffs.debuff;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StaggerEffect extends MobEffect {
    public static String ID = "efea5782-b172-4048-b0cb-48622290b4b3";

    public StaggerEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ID,
                -0.1f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
