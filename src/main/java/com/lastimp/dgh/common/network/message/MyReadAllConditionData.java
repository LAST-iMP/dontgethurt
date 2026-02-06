
package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.enums.OperationType;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.common.capability.HealthCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

public record MyReadAllConditionData(long id_most, long id_least, int entityID, CompoundTag tag, String oper) implements CustomPacketPayload {
    public static final Type<MyReadAllConditionData> TYPE = new Type<>(ResourceHelper.ModResource("my_read_all_condition"));

    public static final StreamCodec<ByteBuf, MyReadAllConditionData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            MyReadAllConditionData::id_most,
            ByteBufCodecs.VAR_LONG,
            MyReadAllConditionData::id_least,
            ByteBufCodecs.VAR_INT,
            MyReadAllConditionData::entityID,
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

    public static MyReadAllConditionData getInstance(UUID uuid, int entityID, CompoundTag tag, OperationType operation) {
        return new MyReadAllConditionData(
                uuid.getMostSignificantBits(),
                uuid.getLeastSignificantBits(),
                entityID,
                tag,
                operation.toString()
        );
    }

    public static HealthCapability getHealthFromInstance(CompoundTag tag, HolderLookup.Provider provider) {
        HealthCapability health = new HealthCapability();
        health.deserialize(provider, tag);
        return health;
    }
}
