package com.lastimp.dgh.common.item.tool;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public class MedicineBag extends AbstractSmallBag {
    public MedicineBag(Properties properties) {
        super(properties);
    }

    @Override
    public IBackpackInventory getBackPackHandler(ItemStack bagStack) {
        var backpack = PlatformService.BACKPACK_FACTORY.get(bagStack, DataComponents.CONTAINER, 9);
        backpack.addAllowed(ModTags.MEDICINE_DIRECT);
        return backpack;
    }
}
