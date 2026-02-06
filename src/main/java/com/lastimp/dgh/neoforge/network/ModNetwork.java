package com.lastimp.dgh.neoforge.network;

import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.neoforge.network.handler.ClientPayloadHandler;
import com.lastimp.dgh.neoforge.network.handler.ServerPayloadHandler;
import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import com.lastimp.dgh.common.network.message.MyKeyPressedData;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import com.lastimp.dgh.common.network.message.MyServerConfigSynData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Utils.MODID)
public class ModNetwork {
    @SubscribeEvent
    public static void registerNetwork(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(Utils.MODID);
        registrar.playBidirectional(
                MyReadAllConditionData.TYPE,
                MyReadAllConditionData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleReadAllConditionData,
                        ServerPayloadHandler::handleReadAllConditionData
                )
        );
        registrar.playToServer(
                MyHealingItemUseData.TYPE,
                MyHealingItemUseData.STREAM_CODEC,
                ServerPayloadHandler::handleHealingItemUsageData
        );
        registrar.playToServer(
                MyKeyPressedData.TYPE,
                MyKeyPressedData.STREAM_CODEC,
                ServerPayloadHandler::handleClientPress
        );
        registrar.playToClient(
                MyServerConfigSynData.TYPE,
                MyServerConfigSynData.STREAM_CODEC,
                ClientPayloadHandler::handleServerConfigSYNData
        );
    }
}
