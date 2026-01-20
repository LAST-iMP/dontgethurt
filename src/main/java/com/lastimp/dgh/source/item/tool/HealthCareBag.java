package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class HealthCareBag extends AbstractSmallBag {

    public HealthCareBag(Properties properties) {
        super(properties);
    }

    @Override
    public BackpackInventory getBackPackHandler(ItemStack bagStack) {
        var backpack = new BackpackInventory(bagStack, DataComponents.CONTAINER, 9);
        backpack.addAllowed(ModTags.MEDICINE);
        backpack.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        return backpack;
    }
}
