package com.lastimp.dgh.fabric.container;

import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventoryNF extends SimpleContainer implements IBackpackInventory {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<TagKey<Item>> disAllowedItemTags = new HashSet<>();

    public BackpackInventoryNF(int size) {
        super(size);
    }

    @Override
    public Set<TagKey<Item>> allowedItemTags() {
        return allowedItemTags;
    }

    @Override
    public Set<TagKey<Item>> disAllowedItemTags() {
        return disAllowedItemTags;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
    }

    @Override
    public int getSlots() {
        return super.getContainerSize();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return super.getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!super.canPlaceItem(slot, stack)) return stack;
        if (!this.isItemValid(slot, stack)) return stack;
        super.setItem(slot, stack);
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        var item = this.getStackInSlot(slot);
        if (!super.canTakeItem(this, slot, item)) return ItemStack.EMPTY;
        if (!this.isItemValid(slot, item)) return ItemStack.EMPTY;
        return super.removeItem(slot, amount);
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(super.getMaxStackSize(), this.getItem(slot).getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        boolean result = this.allowedItemTags.stream().anyMatch(stack::is) && this.disAllowedItemTags.stream().noneMatch(stack::is);
        return result || stack.isEmpty();
    }

    @Override
    public CompoundTag serialize() {
        return this.serialize();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.deserialize(nbt);
    }
}
