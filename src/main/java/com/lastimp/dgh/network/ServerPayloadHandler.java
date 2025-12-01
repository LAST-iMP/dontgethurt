package com.lastimp.dgh.network;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.client.gui.MenuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerPayloadHandler {

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> MyReadAllConditionData.handlerServer(data, ctx));
        context.setPacketHandled(true);
    }

    public static void handleClientPress(final MyKeyPressedData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> MyKeyPressedData.handlerServer(data, ctx));
        context.setPacketHandled(true);
    }

    public static void handleHealingItemUsageData(final MyHealingItemUseData data, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> MyHealingItemUseData.handlerServer(data, ctx));
        context.setPacketHandled(true);
    }
}
