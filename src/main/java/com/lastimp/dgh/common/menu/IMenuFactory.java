package com.lastimp.dgh.common.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.NotNull;

public interface IMenuFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int windowId, Inventory inv, RegistryFriendlyByteBuf data);

    @Override
    default @NotNull T create(int windowId, @NotNull Inventory inv) {
        return create(windowId, inv, null);
    }
}
