package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FoodConsumer extends AbstractDirectHealItems {
    public FoodConsumer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        if (!(entity instanceof Player player)) return false;
        player.addEffect(new MobEffectInstance(ModEffects.FOOD_CONSUMER_EFFECT.get(), 20 * 120));
        return true;
    }
}
