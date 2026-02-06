package com.lastimp.dgh.forge.capability.provider;

import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.forge.container.BackpackInventoryNF;
import com.lastimp.dgh.forge.entry.register.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BagItemInventoryProvider implements ICapabilitySerializable<CompoundTag> {
    private BackpackInventoryNF inv;
    private final LazyOptional<BackpackInventoryNF> optional;

    public BagItemInventoryProvider(BackpackInventoryNF inv, ItemStack stack) {
        this.inv = inv;
        this.optional = LazyOptional.of(() -> this.inv);
        this.deserializeNBT(stack.getOrCreateTag());
    }

    public static IBackpackInventory getBackPackHandler(ItemStack bagStack) {
        return bagStack.getCapability(ModCapabilities.BAG_INV)
                .orElseThrow(() -> new IllegalStateException("No item handler"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapabilities.BAG_INV
                ? optional.cast()
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
