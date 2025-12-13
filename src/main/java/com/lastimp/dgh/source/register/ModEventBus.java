package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthProvider;
import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.ServerPayloadHandler;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                ModCapabilities.HEALTH_HANDLER,
                EntityType.PLAYER,
                new HealthProvider()
        );
    }

    @SubscribeEvent
    public static void registerNetwork(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(DontGetHurt.MODID);
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
    }
}
