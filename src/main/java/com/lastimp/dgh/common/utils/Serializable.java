package com.lastimp.dgh.common.utils;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public interface Serializable {
    CompoundTag serialize(HolderLookup.Provider provider);
    void deserialize(HolderLookup.Provider provider, CompoundTag nbt);
}
