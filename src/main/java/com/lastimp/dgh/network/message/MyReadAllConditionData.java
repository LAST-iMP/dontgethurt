
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.storage.ValueInput;

import java.util.UUID;

public record MyReadAllConditionData(long id_most, long id_least, int entityID, CompoundTag tag, String oper) implements CustomPacketPayload {
    public static final Type<MyReadAllConditionData> TYPE = new Type<>(Common.getId(DontGetHurt.MODID, "my_read_all_condition"));

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

    public static HealthCapability getHealthFromInstance(ValueInput input) {
        HealthCapability health = new HealthCapability();
        health.deserialize(input);
        return health;
    }
}
