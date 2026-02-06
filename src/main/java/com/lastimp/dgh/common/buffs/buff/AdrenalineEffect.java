package com.lastimp.dgh.common.buffs.buff;

import com.lastimp.dgh.common.utils.ResourceHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AdrenalineEffect extends MobEffect {
    public AdrenalineEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ResourceHelper.ModResource("adrenaline_effect"),
                0.1f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceHelper.ModResource("adrenaline_effect"),
                0.1f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
