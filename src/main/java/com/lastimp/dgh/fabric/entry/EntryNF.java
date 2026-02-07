package com.lastimp.dgh.fabric.entry;

import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record EntryNF<T>(Holder.Reference<T> holder) implements IEntry<T> {
    @NotNull
    @Override
    public T get() {
        return this.holder.value();
    }

    @Override
    public ResourceLocation getId() {
        return this.holder.key().location();
    }

    @Nullable
    public ResourceKey<T> getKey() {
        return this.holder.key();
    }

    @Override
    public boolean equals(Object obj) {
        return this.holder.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.holder.hashCode();
    }
}
