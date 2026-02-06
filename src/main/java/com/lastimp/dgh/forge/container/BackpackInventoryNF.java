package com.lastimp.dgh.forge.container;

import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.Set;

public class BackpackInventoryNF extends ItemStackHandler implements IBackpackInventory {
    private final Set<TagKey<Item>> allowedItemTags = new HashSet<>();
    private final Set<TagKey<Item>> disAllowedItemTags = new HashSet<>();

    public BackpackInventoryNF(int size) {
        super(size);
    }

    @Override
    public Set<TagKey<Item>> allowedItemTags() {
        return allowedItemTags;
    }

    @Override
    public Set<TagKey<Item>> disAllowedItemTags() {
        return disAllowedItemTags;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (!super.isItemValid(slot, stack)) return false;
        boolean result = this.allowedItemTags.stream().anyMatch(stack::is) && this.disAllowedItemTags.stream().noneMatch(stack::is);
        return result || stack.isEmpty();
    }

    @Override
    public CompoundTag serialize() {
        return this.serializeNBT();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.deserializeNBT(nbt);
    }
}
