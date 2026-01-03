
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.network.ServerPayloadHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class Network {
    public static SimpleChannel SERVER_INSTANCE;
    public static SimpleChannel CLIENT_INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        SERVER_INSTANCE = NetworkRegistry.newSimpleChannel(
                Common.ResourceLocation(DontGetHurt.MODID, "server_networking"),
                () -> "1.0",
                (s) -> true,
                (s) -> true
        );

        CLIENT_INSTANCE = NetworkRegistry.newSimpleChannel(
                Common.ResourceLocation(DontGetHurt.MODID, "client_networking"),
                () -> "2.0",
                (s) -> true,
                (s) -> true
        );

        CLIENT_INSTANCE.registerMessage(
                nextID(),
                MyReadAllConditionData.class,
                MyReadAllConditionData::toBytes,
                MyReadAllConditionData::new,
                ClientPayloadHandler::handleReadAllConditionData,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        SERVER_INSTANCE.registerMessage(
                nextID(),
                MyReadAllConditionData.class,
                MyReadAllConditionData::toBytes,
                MyReadAllConditionData::new,
                ServerPayloadHandler::handleReadAllConditionData,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        SERVER_INSTANCE.registerMessage(
                nextID(),
                MyKeyPressedData.class,
                MyKeyPressedData::toBytes,
                MyKeyPressedData::new,
                ServerPayloadHandler::handleClientPress,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        SERVER_INSTANCE.registerMessage(
                nextID(),
                MyHealingItemUseData.class,
                MyHealingItemUseData::toBytes,
                MyHealingItemUseData::new,
                ServerPayloadHandler::handleHealingItemUsageData,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}
