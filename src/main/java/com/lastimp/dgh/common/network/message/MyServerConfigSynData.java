package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.config.ModConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public record MyServerConfigSynData(ModConfigs.Type configType, CompoundTag tag) {
    public MyServerConfigSynData(FriendlyByteBuf buffer) {
        this(
                buffer.readEnum(ModConfigs.Type.class),
                buffer.readNbt()
        );
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeEnum(this.configType);
        buf.writeNbt(this.tag);
    }

    public static MyServerConfigSynData getInstance(ModConfigs.Type type) {
        return new MyServerConfigSynData(type, ModConfigs.getCompound(type));
    }
}
