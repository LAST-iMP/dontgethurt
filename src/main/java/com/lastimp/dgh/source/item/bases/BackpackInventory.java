package com.lastimp.dgh.source.item.bases;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.MutableDataComponentHolder;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("removal")
public class BackpackInventory extends net.neoforged.neoforge.items.ComponentItemHandler {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<Item> allowedItems = new HashSet<>();

    public BackpackInventory(MutableDataComponentHolder parent, DataComponentType<ItemContainerContents> component, int size) {
        super(parent, component, size);
    }

    public BackpackInventory addAllowed(TagKey<Item> tags) {
        this.allowedItemTags.add(tags);
        return this;
    }

    public BackpackInventory addAllowed(Item item) {
        this.allowedItems.add(item);
        return this;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!super.isItemValid(slot, stack)) return false;
        for (var tag : this.allowedItemTags)
            if (stack.is(tag)) return true;
        for (var item : this.allowedItems)
            if (stack.is(item)) return true;
        return stack.isEmpty();
    }
}
