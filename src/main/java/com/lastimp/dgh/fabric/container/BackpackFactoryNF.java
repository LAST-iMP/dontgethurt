package com.lastimp.dgh.fabric.container;

import com.lastimp.dgh.common.container.IBackpackFactory;
import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.fabric.capability.provider.BagItemInventoryProvider;
import net.minecraft.world.item.ItemStack;

public class BackpackFactoryNF implements IBackpackFactory {
    @Override
    public IBackpackInventory get(ItemStack itemStack) {
        return BagItemInventoryProvider.getBackPackHandler(itemStack);
    }
}
