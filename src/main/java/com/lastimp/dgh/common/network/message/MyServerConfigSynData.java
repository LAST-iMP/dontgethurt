package com.lastimp.dgh.common.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record MyServerConfigSynData(CompoundTag tag) {
    public MyServerConfigSynData(FriendlyByteBuf buffer) {
        this(buffer.readNbt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    public static MyServerConfigSynData getInstance(CompoundTag tag) {
        return new MyServerConfigSynData(tag);
    }
}
