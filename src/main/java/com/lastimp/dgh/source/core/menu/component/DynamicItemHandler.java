package com.lastimp.dgh.source.core.menu.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashSet;
import java.util.Set;

public class DynamicItemHandler implements IItemHandlerModifiable, INBTSerializable<CompoundTag> {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<Item> allowedItems = new HashSet<>();

    protected final ItemStack[] stacks;
    private final int limit;

    public DynamicItemHandler() {
        this(1, 64);
    }

    public DynamicItemHandler(int size, int limit) {
        this.stacks = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            this.stacks[i] = ItemStack.EMPTY;
        }
        this.limit = limit;
    }

    @Override
    public int getSlots() {
        return stacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return stacks[i] != null ? stacks[i] : ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int i, ItemStack itemStack, boolean simulate) {
        if (!isItemValid(i, itemStack)) return itemStack;
        if (!simulate) {
            this.setStackInSlot(i, itemStack.copyWithCount(1));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int i, int amount, boolean simulate) {
        ItemStack stack = stacks[i];
        if (stack.isEmpty()) return ItemStack.EMPTY;

        if (!simulate) {
            stacks[i] = ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public int getSlotLimit(int i) {
        return stacks[i].isEmpty() ? limit : stacks[i].getMaxStackSize();
    }

    public DynamicItemHandler addAllowed(TagKey<Item> tags) {
        this.allowedItemTags.add(tags);
        return this;
    }

    public DynamicItemHandler addAllowed(Item item) {
        this.allowedItems.add(item);
        return this;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        for (var tag : this.allowedItemTags)
            if (stack.is(tag)) return true;
        for (var item : this.allowedItems)
            if (stack.is(item)) return true;
        return stack.isEmpty();
    }

    @Override
    public void setStackInSlot(int i, ItemStack itemStack) {
        this.stacks[i] = itemStack != null ? itemStack : ItemStack.EMPTY;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < stacks.length; i++) {
            if (!stacks[i].isEmpty()) tag.put("item_"+i, stacks[i].save(provider));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        for (int i = 0; i < stacks.length; i++) {
            if (tag.contains("item_"+i)) {
                stacks[i] = ItemStack.parse(provider, tag.getCompound("item_" + i)).orElse(ItemStack.EMPTY);
            } else {
                stacks[i] = ItemStack.EMPTY;
            }
        }
    }
}
