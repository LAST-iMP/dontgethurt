package com.lastimp.dgh.source.core.bodyPart.base;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractOrgan extends Item {
    public AbstractOrgan(Properties properties) {
        super(properties);
    }

    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        return stack;
    }
}
