package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.network.IPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record MyServerConfigSynData(CompoundTag tag) implements IPayload<MyServerConfigSynData> {
    public MyServerConfigSynData(FriendlyByteBuf buffer) {
        this(buffer.readNbt());
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
    }

    @Override
    public MyServerConfigSynData fromBytes(FriendlyByteBuf buf) {
        return new MyServerConfigSynData(buf);
    }

    public static MyServerConfigSynData getInstance(CompoundTag tag) {
        return new MyServerConfigSynData(tag);
    }
}
