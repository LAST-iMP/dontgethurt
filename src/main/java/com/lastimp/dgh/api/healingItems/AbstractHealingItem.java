package com.lastimp.dgh.api.healingItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractHealingItem extends Item {
    public AbstractHealingItem(Properties properties) {
        super(properties);
    }

    public boolean available(ItemStack stack) {
        return true;
    }

}
