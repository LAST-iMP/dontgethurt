package com.lastimp.dgh.common.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface IBackpackFactory {
    IBackpackInventory get(ItemStack itemStack);

    void set(ItemStack itemStack, IBackpackInventory inventory);

    default NonNullList<ItemStack> getContext(ItemStack itemStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        IBackpackInventory inv = this.get(itemStack);
        for (int i = 0; i < inv.getSlots(); i++) {
            var slotItem = inv.getStackInSlot(i);
            nonnulllist.add(i, slotItem);
        }
        return nonnulllist;
    }
}
