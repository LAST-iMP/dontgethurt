package com.lastimp.dgh.network.message;

import com.google.gson.JsonArray;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.EntityType;

import java.util.Set;

public record MyServerConfigSynData(String healthWhiteList) implements CustomPacketPayload {
    public static final Type<MyServerConfigSynData> TYPE = new Type<>(Common.ResourceLocation(DontGetHurt.MODID, "my_server_config_data"));

    public static final StreamCodec<ByteBuf, MyServerConfigSynData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MyServerConfigSynData::healthWhiteList,
            MyServerConfigSynData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyServerConfigSynData getInstance(Set<EntityType<?>> list) {
        JsonArray array = new JsonArray();
        list.forEach((type) -> array.add(EntityType.getKey(type).toString()));
        return new MyServerConfigSynData(array.toString());
    }
}
