package com.lastimp.dgh.neoforge.container;

import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventoryNF extends ComponentItemHandler implements IBackpackInventory {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<TagKey<Item>> disAllowedItemTags = new HashSet<>();

    public BackpackInventoryNF(MutableDataComponentHolder parent, DataComponentType<ItemContainerContents> component, int size) {
        super(parent, component, size);
    }

    @Override
    public Set<TagKey<Item>> allowedItemTags() {
        return this.allowedItemTags;
    }

    @Override
    public Set<TagKey<Item>> disAllowedItemTags() {
        return this.disAllowedItemTags;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!super.isItemValid(slot, stack)) return false;
        return this.isItemValidTags(stack);
    }
}
