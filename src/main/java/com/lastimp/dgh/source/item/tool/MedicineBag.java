package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class MedicineBag extends AbstractSmallBag {
    public MedicineBag(Properties properties) {
        super(properties);
    }

    @Override
    public BackpackInventory getBackPackHandler(ItemStack bagStack) {
        var backpack = new BackpackInventory(bagStack, DataComponents.CONTAINER, 9);
        backpack.addAllowed(ModTags.MEDICINE_DIRECT);
        return backpack;
    }
}
