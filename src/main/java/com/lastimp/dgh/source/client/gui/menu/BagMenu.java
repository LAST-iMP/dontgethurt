package com.lastimp.dgh.source.client.gui.menu;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.register.ModMenus;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.component.DataComponents;
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
    protected final BackpackInventory handler;
    //服务端
    public BagMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inv, ItemStack bagStack) {
        super(menuType, containerId);
        this.handler = new BackpackInventory(bagStack, DataComponents.CONTAINER, 9);
        layoutPlayerInventorySlots(inv);
    }

    public static class HealthCareBag extends BagMenu {
        public HealthCareBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, inv.player.getInventory().getItem(buf.readInt()));
        }

        public HealthCareBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.HEALTH_CARE_BAG_MENU.get(), pContainerId, inv, bagStack);
            this.handler.addAllowed(ModTags.MEDICINE);
            this.handler.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        }
    }

    public static class SurgeryToolBag extends BagMenu {
        public SurgeryToolBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, inv.player.getInventory().getItem(buf.readInt()));
        }

        public SurgeryToolBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.SURGERY_TOOL_BAG_MENU.get(), pContainerId, inv, bagStack);
            this.handler.addAllowed(ModTags.MEDICAL_TOOLS_SURGERY);
        }
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
                if (!this.moveItemStackTo(stackInSlot, 0, 35, false))
                    return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stackInSlot, 36, 36 + bagSlotCount, false))
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

    private void layoutPlayerInventorySlots(Inventory playerInventory) {
        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 31 + row * 18));
            }
        }
        // Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 89));
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
