package com.lastimp.dgh.fabric.capability.provider;

import com.lastimp.dgh.common.utils.Lazy;
import com.lastimp.dgh.common.utils.Serializable;
import com.lastimp.dgh.fabric.container.BackpackInventoryNF;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BagItemInventoryProvider implements Serializable {
    private BackpackInventoryNF inv;
    private final Lazy<BackpackInventoryNF> optional;

    public BagItemInventoryProvider(BackpackInventoryNF inv, ItemStack stack) {
        this.inv = inv;
        this.optional = Lazy.of(() -> this.inv);
        this.deserialize(stack.getOrCreateTag());
    }

    public @NotNull BackpackInventoryNF getCapability() {
        return optional.get();
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("inv", inv.serialize());
        return tag;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        if (nbt != null)
            inv.deserialize(nbt.getCompound("inv"));
    }
}
