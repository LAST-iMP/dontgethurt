package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
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
                Common.ResourceLocation(DontGetHurt.MODID, "adrenaline_effect"),
                0.2f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
