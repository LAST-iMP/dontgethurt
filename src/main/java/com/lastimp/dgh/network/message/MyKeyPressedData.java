package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.neoforge.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MyKeyPressedData (String key, int index) implements CustomPacketPayload {
    public static final Type<MyKeyPressedData> TYPE = new Type<>( Common.getId(DontGetHurt.MODID, "my_key_press_data"));

    public static final StreamCodec<ByteBuf, MyKeyPressedData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            MyKeyPressedData::key,
            ByteBufCodecs.VAR_INT,
            MyKeyPressedData::index,
            MyKeyPressedData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyKeyPressedData getInstance(KeyPressedType key, int index) {
        return new MyKeyPressedData(key.name(), index);
    }
}
