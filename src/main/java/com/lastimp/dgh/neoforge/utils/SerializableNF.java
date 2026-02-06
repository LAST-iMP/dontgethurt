package com.lastimp.dgh.neoforge.utils;

import com.lastimp.dgh.common.utils.Serializable;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface SerializableNF extends INBTSerializable<CompoundTag>, Serializable {
    default CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return this.serialize(provider);
    }

    default void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.deserialize(provider, nbt);
    }
}
