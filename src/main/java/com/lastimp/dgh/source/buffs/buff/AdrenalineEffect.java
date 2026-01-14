package com.lastimp.dgh.source.buffs.buff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class AdrenalineEffect extends MobEffect {
    public static String ID_ATTACK = "efea50a2-b172-4048-b12b-4862229774a4";
    public static String ID_SPEED = "efea50a2-b172-9888-b12b-4862229774a4";

    public AdrenalineEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);

        AttributeInstance attack = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        AttributeInstance speed = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);

        double amount = 0.1 * (amplifier + 1);
        AttributeModifier attack_modifier = new AttributeModifier(
                UUID.fromString(ID_ATTACK),
                "adrenaline_attack_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        AttributeModifier speed_modifier = new AttributeModifier(
                UUID.fromString(ID_SPEED),
                "adrenaline_attack_effect",
                amount,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        if (attack != null) {
            attack.removeModifier(UUID.fromString(ID_ATTACK));
            attack.addTransientModifier(attack_modifier);
        }
        if (speed != null) {
            speed.removeModifier(UUID.fromString(ID_SPEED));
            speed.addTransientModifier(speed_modifier);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        AttributeInstance attack = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if (attack != null)
            attack.removeModifier(UUID.fromString(ID_ATTACK));
        AttributeInstance speed = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (speed != null)
            speed.removeModifier(UUID.fromString(ID_SPEED));
    }
}
