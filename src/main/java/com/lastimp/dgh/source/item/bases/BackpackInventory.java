package com.lastimp.dgh.source.item.bases;

import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.api.tags.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventory extends ItemStackHandler {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<Item> allowedItems = new HashSet<>();

    public BackpackInventory(int size) {
        super(size);
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
