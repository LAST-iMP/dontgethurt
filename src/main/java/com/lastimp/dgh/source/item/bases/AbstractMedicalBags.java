package com.lastimp.dgh.source.item.bases;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractMedicalBags extends Item {
    public AbstractMedicalBags(Properties properties) {
        super(properties.stacksTo(1));
    }

    public abstract BackpackInventory getBackPackHandler(ItemStack bagStack);
}
