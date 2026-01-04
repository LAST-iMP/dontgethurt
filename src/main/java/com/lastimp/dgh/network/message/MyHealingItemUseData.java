
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.neoforge.Common;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record MyHealingItemUseData (long id_most, long id_least, int slotNum, String component) implements CustomPacketPayload {
    public static final int HAND_PULSE = -255;
    public static final Type<MyHealingItemUseData> TYPE = new Type<>(Common.ResourceLocation(DontGetHurt.MODID, "my_healing_item_use_data"));

    public static final StreamCodec<ByteBuf, MyHealingItemUseData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            MyHealingItemUseData::id_most,
            ByteBufCodecs.VAR_LONG,
            MyHealingItemUseData::id_least,
            ByteBufCodecs.VAR_INT,
            MyHealingItemUseData::slotNum,
            ByteBufCodecs.STRING_UTF8,
            MyHealingItemUseData::component,
            MyHealingItemUseData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyHealingItemUseData getInstance(UUID targetId, int slotNum, BodyComponents components) {
        if (components == null)
            return new MyHealingItemUseData(targetId.getMostSignificantBits(), targetId.getLeastSignificantBits(), slotNum, "NONE");
        else
            return new MyHealingItemUseData(targetId.getMostSignificantBits(), targetId.getLeastSignificantBits(), slotNum, components.name());
    }
}
