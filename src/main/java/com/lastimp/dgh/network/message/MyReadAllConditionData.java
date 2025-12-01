
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.OperationType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record MyReadAllConditionData(long id_most, long id_least, CompoundTag tag, String oper) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<MyReadAllConditionData> TYPE = new CustomPacketPayload.Type<>(Common.ResourceLocation(DontGetHurt.MODID, "my_read_all_condition"));

    public static final StreamCodec<ByteBuf, MyReadAllConditionData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            MyReadAllConditionData::id_most,
            ByteBufCodecs.VAR_LONG,
            MyReadAllConditionData::id_least,
            ByteBufCodecs.COMPOUND_TAG,
            MyReadAllConditionData::tag,
            ByteBufCodecs.STRING_UTF8,
            MyReadAllConditionData::oper,
            MyReadAllConditionData::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static MyReadAllConditionData getInstance(UUID uuid, PlayerHealthCapability health, OperationType operation, HolderLookup.Provider provider) {
        return new MyReadAllConditionData(
                uuid.getMostSignificantBits(),
                uuid.getLeastSignificantBits(),
                health == null ? new CompoundTag() : health.serializeNBT(provider),
                operation.toString()
        );
    }

    public static PlayerHealthCapability getHealthFromInstance(CompoundTag tag, HolderLookup.Provider provider) {
        PlayerHealthCapability health = new PlayerHealthCapability();
        health.deserializeNBT(provider, tag);
        return health;
    }
}
