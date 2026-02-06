package com.lastimp.dgh.common.buffs.debuff;

import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StaggerEffect extends MobEffect {
    public StaggerEffect(int color) {
        super(MobEffectCategory.HARMFUL, color);
        this.setBlendDuration(0);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(Utils.MODID, "stagger_effect"),
                -0.05f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
