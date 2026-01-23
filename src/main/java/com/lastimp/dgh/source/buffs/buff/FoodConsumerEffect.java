package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class FoodConsumerEffect extends MobEffect {
    public FoodConsumerEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);

        this.addAttributeModifier(
                Attributes.MAX_ABSORPTION,
                ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "food_consumer_absorb"),
                2f,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            var food = player.getFoodData();
            if (food.getFoodLevel() > 12 && player.getMaxAbsorption() > player.getAbsorptionAmount()) {
                food.addExhaustion(6);
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 1);
            }
        } else if (livingEntity.getMaxAbsorption() > livingEntity.getAbsorptionAmount()) {
            livingEntity.setAbsorptionAmount(livingEntity.getAbsorptionAmount() + 1);
        }
        return true;
    }
}
