package com.lastimp.dgh.common.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IEntry<T> extends Supplier<T> {
    ResourceLocation getId();

    @Nullable ResourceKey<T> getKey();
}
