
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.api.enums.OperationType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class MyReadAllConditionData {
    private long id_most;
    private long id_least;
    private CompoundTag tag;
    private String oper;

    public MyReadAllConditionData(FriendlyByteBuf buffer) {
        this.id_most = buffer.readLong();
        this.id_least = buffer.readLong();
        this.tag = buffer.readNbt();
        this.oper = buffer.readUtf();
    }

    public MyReadAllConditionData(UUID uuid, HealthCapability health, OperationType operation) {
        this.id_most = uuid.getMostSignificantBits();
        this.id_least = uuid.getLeastSignificantBits();
        this.tag = health != null ? health.serializeNBT() : new CompoundTag();
        this.oper = operation.name();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(this.id_most);
        buf.writeLong(this.id_least);
        buf.writeNbt(this.tag);
        buf.writeUtf(this.oper);
    }

    public static MyReadAllConditionData getInstance(UUID uuid, HealthCapability health, OperationType operation) {
        return new MyReadAllConditionData(uuid, health, operation);
    }

    public static HealthCapability getHealthFromInstance(CompoundTag tag) {
        HealthCapability health = new HealthCapability();
        health.deserializeNBT(tag);
        return health;
    }

    public long id_least() {
        return id_least;
    }

    public long id_most() {
        return id_most;
    }

    public String oper() {
        return oper;
    }

    public CompoundTag tag() {
        return tag;
    }
}
