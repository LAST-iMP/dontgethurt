package com.lastimp.dgh.source.core.menu;

import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.register.ModMenus;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BagMenu extends AbstractContainerMenu {
    private final int Y_SHIFT;
    protected BackpackInventory handler;
    //服务端
    public BagMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inv, int y_shift, ItemStack bagStack) {
        super(menuType, containerId);
        this.Y_SHIFT = y_shift;
        this.handler = ((AbstractSmallBag)bagStack.getItem()).getBackPackHandler(bagStack);
        this.layoutPlayerInventorySlots(inv);
    }

    public static class HealthSmallBag extends BagMenu {
        public HealthSmallBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, inv.player.getInventory().getItem(buf.readInt()));
        }

        public HealthSmallBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.HEALTH_SMALL_BAG_MENU.get(), pContainerId, inv, 0, bagStack);
        }
    }

    public static class MedicineSmallBag extends BagMenu {
        public MedicineSmallBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, inv.player.getInventory().getItem(buf.readInt()));
        }

        public MedicineSmallBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.HEALTH_SMALL_MEDICINE_BAG_MENU.get(), pContainerId, inv, 18, bagStack);
        }
    }

    public ItemStack getStackBySlotNum(int slotNum) {
        if (slotNum >= 36 && this.handler == null) return ItemStack.EMPTY;
        return this.getSlot(slotNum).getItem();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            newStack = stackInSlot.copy();

            var bagSlotCount = handler.getSlots();
            if (index >= 36) {
                if (!this.moveItemStackTo(stackInSlot, 0, 36))
                    return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stackInSlot, 36, 36 + bagSlotCount))
                    return ItemStack.EMPTY;
            }

            slot.setByPlayer(stackInSlot);
            slot.onTake(player, stackInSlot);

            if (stackInSlot.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }
        }
        return newStack;
    }

    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex) {
        boolean flag = false;
        int i = startIndex;

        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (i >= endIndex) break;

                Slot slot = this.getSlot(i);
                ItemStack itemstack = slot.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameComponents(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int k = slot.getMaxStackSize(itemstack);
                    if (j <= k) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.setByPlayer(itemstack);
                        slot.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < k) {
                        stack.shrink(k - itemstack.getCount());
                        itemstack.setCount(k);
                        slot.setByPlayer(itemstack);
                        slot.setChanged();
                        flag = true;
                    }
                }
                ++i;
            }
        }

        if (!stack.isEmpty()) {
            i = startIndex;
            while(true) {
                if (i >= endIndex) break;
                Slot slot1 = this.getSlot(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    int l = slot1.getMaxStackSize(stack);
                    slot1.setByPlayer(stack.split(Math.min(stack.getCount(), l)));
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                ++i;
            }
        }

        return flag;
    }

    protected void layoutPlayerInventorySlots(Inventory playerInventory) {
        // Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 89 + Y_SHIFT));
        }
        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 31 + row * 18 + Y_SHIFT));
            }
        }
        // 背包内部
        for (int row = 0; row < 1; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9;
                this.addSlot(new SlotItemHandler(handler, index, 8 + col * 18, 9 + row * 18));
            }
        }
    }
}
