package com.lastimp.dgh.source.core.menu.component;

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

    public ItemStack insertTo(int begin, int end, ItemStack stack) {
        for (int i = begin; i < end && i < this.getSlots(); i++) {
            var old = this.getStackInSlot(i);
            this.setStackInSlot(i, stack);
            stack = old;
            if (stack.isEmpty()) break;
        }
        return stack;
    }

    public void clear() {
        for (int i = 0; i < this.getSlots(); i++) {
            this.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}
