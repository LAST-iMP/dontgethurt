package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BagItemCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private BackpackInventory inv;

    public BagItemCapabilityProvider(BackpackInventory inv, ItemStack stack) {
        this.inv = inv;
        this.deserializeNBT(stack.getOrCreateTag());
    }

    public static IItemHandler getBackPackHandler(ItemStack bagStack) {
        return bagStack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .orElseThrow(() -> new IllegalStateException("No item handler"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER
                ? LazyOptional.of(() -> inv).cast()
                : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("inv", inv.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt != null)
            inv.deserializeNBT(nbt.getCompound("inv"));
    }
}
