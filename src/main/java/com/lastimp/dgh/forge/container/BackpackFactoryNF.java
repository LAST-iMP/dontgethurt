package com.lastimp.dgh.forge.container;

import com.lastimp.dgh.common.container.IBackpackFactory;
import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.forge.capability.provider.BagItemInventoryProvider;
import net.minecraft.world.item.ItemStack;

public class BackpackFactoryNF implements IBackpackFactory {
    @Override
    public IBackpackInventory get(ItemStack itemStack) {
        return BagItemInventoryProvider.getBackPackHandler(itemStack);
    }
}
