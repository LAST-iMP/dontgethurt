package com.lastimp.dgh.common.container;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.BiPredicate;

public class DynamicValidItemHandler extends DynamicItemHandler implements Iterable<ItemStack>{
    private BiPredicate<Integer, ItemStack> valid = null;

    public DynamicValidItemHandler(int size, int limit) {
        super(size, limit);
    }

    @Override
    public @NotNull Iterator<ItemStack> iterator() {
        return Arrays.stream(this.stacks).iterator();
    }

    public void setValidator(BiPredicate<Integer, ItemStack> valid) {
        this.valid = valid;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (this.valid != null && !this.valid.test(slot, stack)) return false;
        return super.isItemValid(slot, stack);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!this.isItemValid(slot, this.getStackInSlot(slot))) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    public ItemStack insertTo(int begin, int end, ItemStack stack) {
        for (int i = begin; i < end && i < this.getSlots(); i++) {
            var old = this.getStackInSlot(i);
            this.setStackInSlot(i, stack);
            stack = old;
            if (stack.isEmpty()) break;
        }
        return stack;
    }

    public void reorder(int begin, int end) {
        int notEmpty = begin;
        for (int empty = begin; empty < end && empty < this.getSlots() && notEmpty < end && notEmpty < this.getSlots(); empty++) {
            if (!this.getStackInSlot(empty).isEmpty()) continue;
            for (notEmpty = Math.max(empty + 1, notEmpty); notEmpty < end && notEmpty < this.getSlots(); notEmpty++) {
                var target = this.getStackInSlot(notEmpty);
                if (target.isEmpty()) continue;
                this.setStackInSlot(empty, target);
                this.setStackInSlot(notEmpty, ItemStack.EMPTY);
            }
        }
    }
}
