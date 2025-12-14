
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.source.item.tool.BloodScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

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

    public static void handlerClient(final MyReadAllConditionData data, Supplier<NetworkEvent.Context> ctx) {
        HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag());
        OperationType operation = OperationType.valueOf(data.oper());
        if (operation == OperationType.HEALTH_SCANN && ClientPayloadHandler.getHealthScreen() != null) {
            ClientPayloadHandler.getHealthScreen().setHealthData(health);
        } else if (operation == OperationType.BLOOD_SCANN) {
            UUID uuid = new UUID(data.id_most(), data.id_least());
            var player = ctx.get().getSender();
            var target = player.level().getEntitiesOfClass(
                    LivingEntity.class, AABB.ofSize(player.getEyePosition(), 20, 20, 20),
                    (entity) -> entity.getUUID().equals(uuid)
            ).get(0);
            BloodScanner.scanHealth(ctx.get().getSender(), health, target.getScoreboardName());
        } else if (operation == OperationType.SYN) {
            ClientPayloadHandler.setHealth(health);
        }
    }

    public static void handlerServer(final MyReadAllConditionData data, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        UUID uuid = new UUID(data.id_most(), data.id_least());
        ServerPlayer targetPlayer = (ServerPlayer) context.getSender().level().getPlayerByUUID(uuid);
        HealthCapability health = HealthCapability.get(targetPlayer);

        Network.CLIENT_INSTANCE.send(
                PacketDistributor.PLAYER.with(context::getSender),
                MyReadAllConditionData.getInstance(uuid, health, OperationType.valueOf(data.oper()))
        );
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
