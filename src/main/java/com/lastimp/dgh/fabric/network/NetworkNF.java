package com.lastimp.dgh.fabric.network;

import com.lastimp.dgh.common.network.INetwork;
import com.lastimp.dgh.common.network.IPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class NetworkNF implements INetwork {
    @Override
    public void openScreen(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
        ExtendedScreenHandlerFactory factory = new ExtendedScreenHandlerFactory() {
            @Override
            public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return containerSupplier.createMenu(i, inventory, player);
            }

            @Override
            public @NotNull Component getDisplayName() {
                return containerSupplier.getDisplayName();
            }

            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                extraDataWriter.accept(buf);
            }
        };
        player.openMenu(factory);
    }

    @Override
    public <MSG extends IPayload<?>> void sendToServer(MSG payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        payload.toBytes(buf);
        ClientPlayNetworking.send(ModNetwork.PLAY_TO_SERVER.get(payload.getClass()).id(), buf);
    }

    @Override
    public <MSG extends IPayload<?>> void sendToPlayer(ServerPlayer player, MSG payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        payload.toBytes(buf);
        ServerPlayNetworking.send(player, ModNetwork.PLAY_TO_CLIENT.get(payload.getClass()).id(), buf);
    }

    @Override
    public <MSG extends IPayload<?>> void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, MSG payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        payload.toBytes(buf);
        level.players().stream()
                .filter(player -> player.level() == level)
                .filter(player -> !player.is(excluded))
                .filter(player -> player.distanceToSqr(x, y, z) < radius * radius)
                .forEach(player -> ServerPlayNetworking.send(player, ModNetwork.PLAY_TO_CLIENT.get(payload.getClass()).id(), buf));
    }
}
