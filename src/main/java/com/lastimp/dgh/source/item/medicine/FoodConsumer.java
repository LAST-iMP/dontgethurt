package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class FoodConsumer extends AbstractDirectHealItems {
    public FoodConsumer(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull LivingEntity source, @NotNull LivingEntity entity) {
        return false;
    }
}
