package com.lastimp.dgh.common.container;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public interface IBackpackFactory {
    IBackpackInventory get(ItemStack itemStack, DataComponentType<ItemContainerContents> component, int size);

    default NonNullList<ItemStack> getContext(ItemStack itemStack, DataComponentType<ItemContainerContents> component, int size) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        IBackpackInventory inv = this.get(itemStack, component, size);
        for (int i = 0; i < inv.getSlots(); i++) {
            var slotItem = inv.getStackInSlot(i);
            nonnulllist.add(i, slotItem);
        }
        return nonnulllist;
    }
}
