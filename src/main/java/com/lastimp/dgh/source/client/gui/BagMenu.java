package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class BagMenu extends AbstractContainerMenu {
    private final IItemHandler handler;

    //服务端
    public BagMenu(@Nullable MenuType<?> menuType, int containerId, Inventory inv, ItemStack bagStack) {
        super(menuType, containerId);
        this.handler = bagStack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("No item handler"));
        layoutPlayerInventorySlots(inv);
    }

    public static class HealthCareBag extends BagMenu {
        public HealthCareBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, buf.readItem());
        }

        public HealthCareBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.HEALTH_CARE_BAG_MENU.get(), pContainerId, inv, bagStack);
        }
    }

    public static class SurgeryToolBag extends BagMenu {
        public SurgeryToolBag(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
            this(pContainerId, inv, buf.readItem());
        }

        public SurgeryToolBag(int pContainerId, Inventory inv, ItemStack bagStack) {
            super(ModMenus.SURGERY_TOOL_BAG_MENU.get(), pContainerId, inv, bagStack);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
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
