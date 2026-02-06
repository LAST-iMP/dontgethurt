package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.enums.KeyPressedType;
import com.lastimp.dgh.common.utils.ResourceHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record MyKeyPressedData (KeyPressedType key, int index) implements CustomPacketPayload {
    public static final Type<MyKeyPressedData> TYPE = new Type<>( ResourceHelper.ModResource("my_key_press_data"));

    public static final StreamCodec<ByteBuf, MyKeyPressedData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(
                    KeyPressedType::valueOf,
                    KeyPressedType::name
            ),
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
        return new MyKeyPressedData(key, index);
    }
}
