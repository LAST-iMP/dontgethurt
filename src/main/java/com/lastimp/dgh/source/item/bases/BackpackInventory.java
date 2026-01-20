package com.lastimp.dgh.source.item.bases;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventory extends ItemStackHandler {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<TagKey<Item>> disAllowedItemTags = new HashSet<>();

    public BackpackInventory(int size) {
        super(size);
    }

    public void addAllowed(TagKey<Item> tags) {
        this.allowedItemTags.add(tags);
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
