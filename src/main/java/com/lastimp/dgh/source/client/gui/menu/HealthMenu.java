
package com.lastimp.dgh.source.client.gui.menu;

import com.lastimp.dgh.source.client.gui.component.DynamicSlotItemHandler;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealthMenu extends AbstractContainerMenu {
    protected BackpackInventory handler;
    private final List<DynamicSlotItemHandler> bagSlots = new ArrayList<>();
    public final UUID targetEntity;
    public final boolean isDevice;

    public HealthMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, buf.readUUID(), buf.readBoolean());
    }

    public HealthMenu(int pContainerId, Inventory inv, UUID targetEntity, boolean isDevice) {
        super(ModMenus.HEALTH_MENU.get(), pContainerId);
        this.targetEntity = targetEntity;
        this.isDevice = isDevice;
        layoutPlayerInventorySlots(inv);
    }

    public void openBag(ItemStack stack) {
        if (stack.is(ModItems.HEALTH_CARE_BAG))
            this.handler = BagMenu.HealthCareBag.getBackPackHandler(stack);
        else if (stack.is(ModItems.SURGERY_TOOL_BAG))
            this.handler = BagMenu.SurgeryToolBag.getBackPackHandler(stack);
        this.setBagHandler(this.handler);
    }

    public void closeBag() {
        handler = null;
        this.setBagHandler(null);
    }

    private void setBagHandler(BackpackInventory handler) {
        for (var slot : bagSlots) {
            slot.setHandler(handler);
        }
    }

    public ItemStack getStackBySlotNum(int slotNum) {
        if (slotNum >= 36 && this.handler == null) return ItemStack.EMPTY;
        return this.getSlot(slotNum).getItem();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            newStack = stackInSlot.copy();

            if (index < 9) {
                if (!this.moveItemStackTo(stackInSlot, 9, 35, false))
                    return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stackInSlot, 0, 8, false))
                    return ItemStack.EMPTY;
            }

            if (stackInSlot.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // 添加玩家背包的slot和热键的栏的slot
    private void layoutPlayerInventorySlots(Inventory playerInventory) {
        // Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 41 + i * 18, 188));
        }
        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 41 + col * 18, 130 + row * 18));
            }
        }
        // 动态背包内部
        for (int row = 0; row < 9; row++) {
            int index = row;
            var newSlot = new DynamicSlotItemHandler(null, index, 234, 22 + row * 18);
            this.addSlot(newSlot);
            this.bagSlots.add(newSlot);
        }
    }
}
