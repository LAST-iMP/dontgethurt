
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.message.MyServerConfigSynData;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Objects;

public class ClientPayloadHandler {
    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            OperationType operation = OperationType.valueOf(data.oper());
            if (operation == OperationType.HEALTH_SCANN && ClientAccessor.healthScreen() != null) {
                HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag(), context.player().registryAccess());
                ClientAccessor.healthScreen().setHealthData(health);
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
        context.enqueueWork(() -> HealthLivingEntityList.loadServerData(data.healthWhiteList()))
        .exceptionally(e -> {
            context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
            return null;
        });
    }
}
