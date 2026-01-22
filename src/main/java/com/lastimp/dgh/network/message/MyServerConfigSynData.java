package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MyServerConfigSynData(CompoundTag tag) implements CustomPacketPayload {
    public static final Type<MyServerConfigSynData> TYPE = new Type<>(Common.ResourceLocation(DontGetHurt.MODID, "my_server_config_data"));

    public static final StreamCodec<ByteBuf, MyServerConfigSynData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            MyServerConfigSynData::tag,
            MyServerConfigSynData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyServerConfigSynData getInstance(CompoundTag tag) {
        return new MyServerConfigSynData(tag);
    }
}
