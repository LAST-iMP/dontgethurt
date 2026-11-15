
package com.lastimp.dgh.neoforge.network.handler;

import com.lastimp.dgh.api.client.DGH_ClientAPI;
import com.lastimp.dgh.common.config.ModConfigs;
import com.lastimp.dgh.common.enums.OperationType;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import com.lastimp.dgh.common.network.message.MyServerConfigSynData;
import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ClientPayloadHandler {
    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            OperationType operation = OperationType.valueOf(data.oper());
            UUID uuid = new UUID(data.id_most(), data.id_least());
            if (operation == OperationType.HEALTH_SCANN) {
                DGH_ClientAPI.updateHealthDisplayData(uuid, data.tag());
            } else if (operation == OperationType.SYN) {
                var entity = ClientAccessor.getLiving(data.entityID());
                if (entity != null && HealthCapability.has(entity)) {
                    HealthCapability.getAndApply(entity, health -> health.lightDeserializeNBT(data.tag()));
                }
            }
        })
        .exceptionally(e -> {
            context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
            return null;
        });
    }

    public static void handleServerConfigSYNData(final MyServerConfigSynData data, final IPayloadContext context) {
        context.enqueueWork(() -> ModConfigs.loadServerData(data.configType(), data.tag()))
        .exceptionally(e -> {
            context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
            return null;
        });
    }
}
