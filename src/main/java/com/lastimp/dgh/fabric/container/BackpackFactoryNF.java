package com.lastimp.dgh.fabric.container;

import com.lastimp.dgh.common.container.IBackpackFactory;
import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import com.lastimp.dgh.fabric.capability.BagHolder;
import net.minecraft.world.item.ItemStack;

public class BackpackFactoryNF implements IBackpackFactory {
    @Override
    public IBackpackInventory get(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof AbstractSmallBag)) return null;
        var provider = ((BagHolder) (Object) itemStack).dgh$getBagProvider();
        provider.deserialize(itemStack.getOrCreateTag().getCompound("dgh_inv"));
        return provider.getCapability();
    }

    @Override
    public void set(ItemStack itemStack, IBackpackInventory inventory) {
        itemStack.addTagElement("dgh_inv", inventory.serialize());
    }
}
