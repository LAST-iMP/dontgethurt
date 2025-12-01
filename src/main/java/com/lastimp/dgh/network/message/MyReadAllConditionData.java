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

import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.source.item.tool.BloodScanner;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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

    public MyReadAllConditionData(UUID uuid, PlayerHealthCapability health, OperationType operation) {
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
        PlayerHealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag());
        OperationType operation = OperationType.valueOf(data.oper());
        if (operation == OperationType.HEALTH_SCANN && ClientPayloadHandler.getHealthScreen() != null) {
            ClientPayloadHandler.getHealthScreen().setHealthData(health);
        } else if (operation == OperationType.BLOOD_SCANN) {
            UUID uuid = new UUID(data.id_most(), data.id_least());
            BloodScanner.scanHealth(Minecraft.getInstance().player, health, Minecraft.getInstance().level.getPlayerByUUID(uuid).getScoreboardName());
        }
    }

    public static void handlerServer(final MyReadAllConditionData data, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        UUID uuid = new UUID(data.id_most(), data.id_least());
        ServerPlayer targetPlayer = (ServerPlayer) context.getSender().level().getPlayerByUUID(uuid);
        PlayerHealthCapability health = PlayerHealthCapability.get(targetPlayer);

        Network.CLIENT_INSTANCE.send(
                PacketDistributor.PLAYER.with(context::getSender),
                MyReadAllConditionData.getInstance(uuid, health, OperationType.valueOf(data.oper()))
        );
    }


    public static MyReadAllConditionData getInstance(UUID uuid, PlayerHealthCapability health, OperationType operation) {
        return new MyReadAllConditionData(uuid, health, operation);
    }

    public static PlayerHealthCapability getHealthFromInstance(CompoundTag tag) {
        PlayerHealthCapability health = new PlayerHealthCapability();
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
