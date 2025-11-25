package com.lastimp.dgh.source.item.bases;

import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.healingItems.AbstractHealingTools;
import com.lastimp.dgh.api.tags.ModTags;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;

public class BackpackInventory extends ComponentItemHandler {

    public BackpackInventory(MutableDataComponentHolder parent, DataComponentType<ItemContainerContents> component, int size) {
        super(parent, component, size);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!super.isItemValid(slot, stack)) return false;
        if (stack.is(ModTags.SHEARS)) return true;
        if (stack.getItem() instanceof AbstractHealingItem) return true;
        if (stack.getItem() instanceof AbstractHealingTools) return true;
        if (stack == ItemStack.EMPTY) return true;
        return false;
    }
}
