/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
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
    public static final CustomPacketPayload.Type<MyReadAllConditionData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, "my_read_all_condition"));

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
