package com.lastimp.dgh.fabric.network;

import com.lastimp.dgh.common.network.INetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Consumer;

public class NetworkNF implements INetwork {
    @Override
    public void openScreen(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
        NetworkHooks.openScreen(player, containerSupplier, extraDataWriter);
    }

    @Override
    public <MSG> void sendToServer(MSG payload) {
        ModNetwork.SERVER_INSTANCE.sendToServer(payload);
    }

    @Override
    public <MSG> void sendToPlayer(ServerPlayer player, MSG payload) {
        ModNetwork.CLIENT_INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), payload);
    }

    @Override
    public <MSG> void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, MSG payload) {
        ModNetwork.CLIENT_INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, level.dimension())), payload);
    }
}
