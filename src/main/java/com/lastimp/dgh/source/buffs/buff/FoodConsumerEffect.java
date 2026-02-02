package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class FoodConsumerEffect extends MobEffect {
    public FoodConsumerEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 60 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (!HealthCapability.has(livingEntity)) return;
        HealthCapability.getAndApply(livingEntity, h -> {
            if (livingEntity instanceof Player player) {
                var food = player.getFoodData();
                if (food.getFoodLevel() < 12) return;
                food.addExhaustion(6);
            }
            float block = 0.5f / 3f / (livingEntity.getMaxHealth() * Config.body_life_factor);
            for (var component : BodyComponents.VISIBLE_BODIES) {
                h.getComponent(component).addConditionHidden(BURN_RES, block);
                h.getComponent(component).addConditionHidden(INTERNAL_RES, block);
                h.getComponent(component).addConditionHidden(OPEN_WOUND_RES, block);
            }
        });
    }
}
