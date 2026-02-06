package com.lastimp.dgh.neoforge.container;

import com.lastimp.dgh.common.container.IBackpackFactory;
import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class BackpackFactoryNF implements IBackpackFactory {
    @Override
    public IBackpackInventory get(ItemStack itemStack, DataComponentType<ItemContainerContents> component, int size) {
        return new BackpackInventoryNF(itemStack, component, size);
    }
}
