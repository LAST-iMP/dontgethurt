
package com.lastimp.dgh.fabric.network;

import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import com.lastimp.dgh.common.network.message.MyKeyPressedData;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import com.lastimp.dgh.common.network.message.MyServerConfigSynData;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.fabric.network.handler.ClientPayloadHandler;
import com.lastimp.dgh.fabric.network.handler.ServerPayloadHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public class ModNetwork {
    public static Map<Class<?>, NetworkPacket> PLAY_TO_CLIENT;
    public static Map<Class<?>, NetworkPacket> PLAY_TO_SERVER;

    public static void registerMessage() {
        PLAY_TO_CLIENT.put(MyReadAllConditionData.class, new NetworkPacket(
                ResourceHelper.ModResource("MyReadAllConditionData")
        ));
        PLAY_TO_CLIENT.put(MyServerConfigSynData.class, new NetworkPacket(
                ResourceHelper.ModResource("MyServerConfigSynData")
        ));

        PLAY_TO_SERVER.put(MyReadAllConditionData.class, new NetworkPacket(
                ResourceHelper.ModResource("MyReadAllConditionData")
        ));
        PLAY_TO_SERVER.put(MyKeyPressedData.class, new NetworkPacket(
                ResourceHelper.ModResource("MyKeyPressedData")
        ));
        PLAY_TO_SERVER.put(MyHealingItemUseData.class, new NetworkPacket(
                ResourceHelper.ModResource("MyHealingItemUseData")
        ));

        CLIENT_INSTANCE.registerMessage(
                nextID(),
                MyReadAllConditionData.class,
                MyReadAllConditionData::toBytes,
                MyReadAllConditionData::new,
                ClientPayloadHandler::handleReadAllConditionData,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CLIENT_INSTANCE.registerMessage(
                nextID(),
                MyServerConfigSynData.class,
                MyServerConfigSynData::toBytes,
                MyServerConfigSynData::new,
                ClientPayloadHandler::handleServerConfigSYNData,
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

    public record NetworkPacket(
            ResourceLocation id
    ) { }
}
