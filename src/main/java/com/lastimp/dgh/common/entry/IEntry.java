package com.lastimp.dgh.common.entry;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface IEntry<R, T extends R> extends Holder<R>, Supplier<T> {
    ResourceLocation getId();
}
