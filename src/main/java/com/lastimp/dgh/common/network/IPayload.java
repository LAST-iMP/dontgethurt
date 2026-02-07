package com.lastimp.dgh.common.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IPayload<T> {
    void toBytes(FriendlyByteBuf buf);
    T fromBytes(FriendlyByteBuf buf);
}
