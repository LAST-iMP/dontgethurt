package com.lastimp.dgh.neoforge.menu;

import com.lastimp.dgh.common.menu.IMenuFactory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.IContainerFactory;
import org.jetbrains.annotations.NotNull;

public class MenuFactoryNF<T extends AbstractContainerMenu> implements IMenuFactory<T>, IContainerFactory<T> {
    private final IMenuFactory<T> factory;

    public MenuFactoryNF(IMenuFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull T create(int windowId, Inventory inv, RegistryFriendlyByteBuf data) {
        return factory.create(windowId, inv, data);
    }

    @Override
    public @NotNull T create(int windowId, @NotNull Inventory inv) {
        return IMenuFactory.super.create(windowId, inv);
    }
}
