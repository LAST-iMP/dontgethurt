package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.config.ModConfigs;
import com.lastimp.dgh.common.utils.ResourceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MyServerConfigSynData(ModConfigs.Type configType, CompoundTag tag) implements CustomPacketPayload {
    public static final Type<MyServerConfigSynData> TYPE = new Type<>(ResourceHelper.ModResource("my_server_config_data"));

    public static final StreamCodec<ByteBuf, MyServerConfigSynData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(
                    ModConfigs.Type::valueOf,
                    ModConfigs.Type::name
            ),
            MyServerConfigSynData::configType,
            ByteBufCodecs.COMPOUND_TAG,
            MyServerConfigSynData::tag,
            MyServerConfigSynData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyServerConfigSynData getInstance(ModConfigs.Type type) {
        return new MyServerConfigSynData(type, ModConfigs.getCompound(type));
    }
}
