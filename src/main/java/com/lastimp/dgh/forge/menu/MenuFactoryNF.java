package com.lastimp.dgh.forge.menu;

import com.lastimp.dgh.common.menu.IMenuFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.IContainerFactory;

public class MenuFactoryNF<T extends AbstractContainerMenu> implements IMenuFactory<T>, IContainerFactory<T> {
    private final IMenuFactory<T> factory;

    public MenuFactoryNF(IMenuFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T create(int windowId, Inventory inv, FriendlyByteBuf data) {
        return factory.create(windowId, inv, data);
    }

    @Override
    public T create(int windowId, Inventory inv) {
        return IMenuFactory.super.create(windowId, inv);
    }
}
