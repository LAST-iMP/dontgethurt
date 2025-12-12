package com.lastimp.dgh.source.client.gui.component;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class DynamicSlotItemHandler extends Slot {
    private static Container emptyInventory = new SimpleContainer(0);
    private int index;
    private IItemHandler handler;

    public DynamicSlotItemHandler(IItemHandler handler, int index, int x, int y) {
        super(emptyInventory, index, x, y);
        this.handler = handler;
        this.index = index;
    }

    public void setHandler(IItemHandler handler) {
        this.handler = handler;
        if (handler != null)
            this.set(this.handler.getStackInSlot(index));
        else
            this.set(ItemStack.EMPTY);
    }

    public boolean mayPlace(ItemStack stack) {
        if (handler == null) return false;
        return !stack.isEmpty() && this.handler.isItemValid(this.getSlotIndex(), stack);
    }

    public int getMaxStackSize() {
        if (handler == null) return 0;
        return this.handler.getSlotLimit(this.index);
    }

    public int getMaxStackSize(ItemStack stack) {
        return Math.min(stack.getMaxStackSize(), this.getMaxStackSize());
    }

    public ItemStack getItem() {
        if (handler == null) return ItemStack.EMPTY;
        return this.handler.getStackInSlot(this.index);
    }

    public void set(ItemStack stack) {
        if (handler == null) return;
        ((IItemHandlerModifiable)this.handler).setStackInSlot(this.index, stack);
        this.setChanged();
    }

    public void onQuickCraft(ItemStack oldStackIn, ItemStack newStackIn) {
    }

    public boolean mayPickup(Player playerIn) {
        if (handler == null) return false;
        return !this.handler.extractItem(this.index, 1, true).isEmpty();
    }

    public ItemStack remove(int amount) {
        if (handler == null) return ItemStack.EMPTY;
        return this.handler.extractItem(this.index, amount, false);
    }
}