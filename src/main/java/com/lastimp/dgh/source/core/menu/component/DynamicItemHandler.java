package com.lastimp.dgh.source.core.menu.component;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.*;

@SuppressWarnings("removal")
public class DynamicItemHandler implements net.neoforged.neoforge.items.IItemHandlerModifiable, ValueIOSerializable {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<Item> allowedItems = new HashSet<>();

    private final ItemStack[] stacks;
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
            stacks[i] = itemStack.copyWithCount(1);
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
        this.stacks[i] = itemStack;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        for (int i = 0; i < this.stacks.length; i++) {
            String key = "item_" + i;
            ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, this.stacks[i]).ifSuccess(tag -> valueOutput.putString(key, tag.toString()));
        }
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        for (int i = 0; i < this.stacks.length; i++) {
            String key = "item_" + i;
            int finalI = i;
            this.stacks[finalI] = ItemStack.EMPTY;
            valueInput.getString(key).ifPresent(strTag -> {
                try {
                    var result = ItemStack.CODEC.parse(NbtOps.INSTANCE, TagParser.parseCompoundFully(strTag));
                    result.ifSuccess(stack -> this.stacks[finalI] = stack);
                } catch (CommandSyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
