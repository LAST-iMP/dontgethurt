package com.lastimp.dgh.source.item.bases;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import net.neoforged.neoforge.items.ComponentItemHandler;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventory extends ComponentItemHandler {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<TagKey<Item>> disAllowedItemTags = new HashSet<>();

    public BackpackInventory(MutableDataComponentHolder parent, DataComponentType<ItemContainerContents> component, int size) {
        super(parent, component, size);
    }

    public BackpackInventory addAllowed(TagKey<Item> tags) {
        this.allowedItemTags.add(tags);
        return this;
    }

    public void addDisAllowed(TagKey<Item> tags) {
        this.disAllowedItemTags.add(tags);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!super.isItemValid(slot, stack)) return false;
        boolean result = this.allowedItemTags.stream().anyMatch(stack::is) && this.disAllowedItemTags.stream().noneMatch(stack::is);
        return result || stack.isEmpty();
    }
}
