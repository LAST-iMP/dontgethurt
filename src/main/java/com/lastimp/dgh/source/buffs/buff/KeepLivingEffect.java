package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class KeepLivingEffect extends MobEffect {

    public KeepLivingEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void onEffectStarted(LivingEntity livingEntity, int amplifier) {
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                Identifier.fromNamespaceAndPath(DontGetHurt.MODID, "living_movement"),
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_SPEED,
                Identifier.fromNamespaceAndPath(DontGetHurt.MODID, "living_attack"),
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
        this.addAttributeModifier(
                Attributes.BLOCK_BREAK_SPEED,
                Identifier.fromNamespaceAndPath(DontGetHurt.MODID, "living_break"),
                0.015f,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
