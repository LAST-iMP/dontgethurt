package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.network.ServerPayloadHandler;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.capability.HealthProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModEventBus {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerEntity(
                ModCapabilities.HEALTH_HANDLER,
                EntityType.PLAYER,
                new HealthProvider()
        );
        HealthProvider.add(ServerPlayer.class);
        event.registerEntity(
                ModCapabilities.HEALTH_HANDLER,
                EntityType.VILLAGER,
                new HealthProvider()
        );
        HealthProvider.add(Villager.class);
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

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;

        var player = event.getEntity();

        var data = player.getPersistentData();
        var key = "dgh_new_player";

        if (!data.getBoolean(key)) {
            player.getInventory().add(new ItemStack(ModItems.HEALTH_CARE_BAG.get()));
            player.getInventory().add(new ItemStack(ModItems.BANDAGE.get(), 8));
            player.getInventory().add(new ItemStack(ModItems.MORPHINE.get(), 2));
            data.putBoolean(key, true);
        }
    }
}
