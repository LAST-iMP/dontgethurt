package com.lastimp.dgh.source.buffs.buff;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FoodConsumerEffect extends MobEffect {
    public FoodConsumerEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            var food = player.getFoodData();
            if (food.getFoodLevel() > 12) {
                food.addExhaustion(6);
                player.setAbsorptionAmount(player.getAbsorptionAmount() + 1);
            }
        } else {
            livingEntity.setAbsorptionAmount(livingEntity.getAbsorptionAmount() + 1);
        }
    }
}
