package com.lastimp.dgh.fabric.utils;

import com.lastimp.dgh.common.utils.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface SerializableNF extends INBTSerializable<CompoundTag>, Serializable {
    default CompoundTag serializeNBT() {
        return this.serialize();
    }

    default void deserializeNBT(CompoundTag nbt) {
        this.deserialize(nbt);
    }
}
