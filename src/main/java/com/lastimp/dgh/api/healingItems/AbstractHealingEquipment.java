package com.lastimp.dgh.api.healingItems;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHealingEquipment extends AbstractDirectHealItems{
    public AbstractHealingEquipment(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull LivingEntity entity) {
        return this.heal(entity);
    }

    public abstract boolean heal(@NotNull LivingEntity entity);

    public abstract int getMaxCooldown();
}
