package com.lastimp.dgh.common.container;

import net.minecraft.world.item.ItemStack;

public interface IItemHandler {
    void setStackInSlot(int slot, ItemStack stack);
    int getSlots();
    ItemStack getStackInSlot(int slot);
    ItemStack insertItem(int slot, ItemStack stack, boolean simulate);
    ItemStack extractItem(int slot, int amount, boolean simulate);
    int getSlotLimit(int slot);
    boolean isItemValid(int slot, ItemStack stack);
}
