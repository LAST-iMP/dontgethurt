package com.lastimp.dgh.common.utils;

import net.minecraft.nbt.CompoundTag;

public interface Serializable {
    CompoundTag serialize();
    void deserialize(CompoundTag nbt);
}
