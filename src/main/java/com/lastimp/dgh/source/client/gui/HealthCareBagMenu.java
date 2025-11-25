package com.lastimp.dgh.source.client.gui;

import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HealthCareBagMenu extends AbstractContainerMenu {
    private final IItemHandler handler;

    //客户端
    public HealthCareBagMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, buf.readItem());
    }

    //服务端
    public HealthCareBagMenu(int pContainerId, Inventory inv, ItemStack bagStack) {
        super(ModMenus.HEALTHCARE_BAG_MENU.get(), pContainerId);
        this.handler = bagStack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("No item handler"));
        layoutPlayerInventorySlots(inv);
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
