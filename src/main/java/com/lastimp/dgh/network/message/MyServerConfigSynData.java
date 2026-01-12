package com.lastimp.dgh.network.message;

import com.google.gson.JsonArray;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;

import java.util.Set;

public record MyServerConfigSynData(String healthWhiteList) {
    public MyServerConfigSynData(FriendlyByteBuf buffer) {
        this(buffer.readUtf());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.healthWhiteList);
    }

    public static MyServerConfigSynData getInstance(Set<EntityType<?>> list) {
        JsonArray array = new JsonArray();
        list.forEach((type) -> array.add(EntityType.getKey(type).toString()));
        return new MyServerConfigSynData(array.toString());
    }
}
