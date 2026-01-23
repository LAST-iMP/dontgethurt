
package com.lastimp.dgh.source.core.menu;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.menu.component.DynamicItemHandler;
import com.lastimp.dgh.source.core.menu.component.DynamicSlot;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealthMenu extends AbstractContainerMenu {
    protected DynamicItemHandler organHandler;
    protected BackpackInventory bagHandler;
    private ItemStack bagStack;
    private final List<DynamicSlot> bagSlots = new ArrayList<>();
    private final List<DynamicSlot> equipments = new ArrayList<>();
    private final List<DynamicSlot> organs = new ArrayList<>(36);
    public final UUID targetEntity;
    public final boolean isDevice;

    public HealthMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, buf.readUUID(), buf.readBoolean());
    }

    public HealthMenu(int pContainerId, Inventory inv, UUID targetEntity, boolean isDevice) {
        this(ModMenus.HEALTH_MENU.get(), pContainerId, inv, targetEntity, isDevice);
    }

    protected HealthMenu(MenuType<?> type, int pContainerId, Inventory inv, UUID targetEntity, boolean isDevice) {
        super(type, pContainerId);
        this.targetEntity = targetEntity;
        this.isDevice = isDevice;
        layoutPlayerInventorySlots(inv);
        if (inv.player.level() instanceof ServerLevel serverLevel) {
            var entity = Utils.getLivingWithHealth(serverLevel, targetEntity);
            if (entity != null) {
                HealthCapability.getAndApply(entity, this::setEquipments);
            }
        }
    }

    public void openBag(ItemStack stack) {
        this.bagStack = stack;
        this.bagHandler = ((AbstractSmallBag)stack.getItem()).getBackPackHandler(stack);
        this.setBagHandler(this.bagHandler);
    }

    public void closeBag() {
        if (this.bagHandler != null) {
            this.bagHandler = null;
            this.bagStack = null;
            this.setBagHandler(null);
        }
    }

    public ItemStack getBag() {
        return this.bagStack;
    }

    private void setBagHandler(BackpackInventory handler) {
        for (var slot : bagSlots) {
            slot.setHandler(handler);
        }
    }

    public void setEquipments(HealthCapability health) {
        equipments.get(0).setHandler(health.oxygenMask());
        equipments.get(1).setHandler(health.autoPulse());
    }

    public ItemStack getStackBySlotNum(int slotNum) {
        if (slotNum >= 47 && this.organHandler == null) return ItemStack.EMPTY;
        if (slotNum >= 36 && this.bagHandler == null) return ItemStack.EMPTY;
        return this.getSlot(slotNum).getItem();
    }

    public void setOrganActive(boolean active, AbstractVisibleBody body) {
        for (var slot : this.organs) {
            slot.setActive(active);
            slot.setHandler(active ? body.organ() : null);
        }
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
        if (this.bagStack != null && !player.getInventory().contains(this.bagStack)) {
            this.closeBag();
        }
        return !HealthCapability.isDying(player) || player.hasEffect(ModEffects.ADRENALINE_EFFECT);
    }

    // 添加玩家背包的slot和热键的栏的slot
    protected void layoutPlayerInventorySlots(Inventory playerInventory) {
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
            var newSlot = new DynamicSlot(null, index, 234, 22 + row * 18);
            this.addSlot(newSlot);
            this.bagSlots.add(newSlot);
        }
        //装备槽位
        var oxygenMask = new DynamicSlot(null, 0, 210, 130);
        this.addSlot(oxygenMask);
        this.equipments.add(oxygenMask);

        var autoPulse = new DynamicSlot(null, 0, 210, 130 + 18);
        this.addSlot(autoPulse);
        this.equipments.add(autoPulse);
        //器官槽位
        int organIndex = 0;
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 6; ++col) {
                var newSlot = new DynamicSlot(null, organIndex++, 104 + col * 18, 12 + row * 18);
                newSlot.setActive(false);
                this.addSlot(newSlot);
                this.organs.add(newSlot);
            }
        }
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 6; ++col) {
                var newSlot = new DynamicSlot(null, organIndex++, 104 + col * 18, 49 + row * 18);
                newSlot.setActive(false);
                this.addSlot(newSlot);
                this.organs.add(newSlot);
            }
        }
        for (int row = 0; row < 2; ++row) {
            for (int col = 0; col < 6; ++col) {
                var newSlot = new DynamicSlot(null, organIndex++, 104 + col * 18, 86 + row * 18);
                newSlot.setActive(false);
                this.addSlot(newSlot);
                this.organs.add(newSlot);
            }
        }
    }
}
