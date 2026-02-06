package com.lastimp.dgh.common.container;

import com.lastimp.dgh.common.utils.Serializable;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public interface IBackpackInventory extends IItemHandler, Serializable {
    Set<TagKey<Item>> allowedItemTags();

    Set<TagKey<Item>> disAllowedItemTags();

    default IBackpackInventory addAllowed(TagKey<Item> tags) {
        this.allowedItemTags().add(tags);
        return this;
    }

    default void addDisAllowed(TagKey<Item> tags) {
        this.disAllowedItemTags().add(tags);
    }

    default boolean isItemValidTags(ItemStack stack) {
        boolean result = this.allowedItemTags().stream().anyMatch(stack::is) && this.disAllowedItemTags().stream().noneMatch(stack::is);
        return result || stack.isEmpty();
    }
}
