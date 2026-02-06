package com.lastimp.dgh.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

import java.util.function.Consumer;

public interface INetwork {
    void openScreen(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter);
    <MSG> void sendToServer(MSG payload);
    <MSG> void sendToPlayer(ServerPlayer player, MSG payload);
    <MSG> void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, MSG payload);
}
