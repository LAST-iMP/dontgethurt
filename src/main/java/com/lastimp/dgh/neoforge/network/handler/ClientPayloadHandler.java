
package com.lastimp.dgh.neoforge.network.handler;

import com.lastimp.dgh.common.enums.OperationType;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import com.lastimp.dgh.common.network.message.MyServerConfigSynData;
import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.client.gui.GuiOpenWrapper;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            OperationType operation = OperationType.valueOf(data.oper());
            if (operation == OperationType.HEALTH_SCANN && GuiOpenWrapper.healthScreen() != null) {
                HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag(), context.player().registryAccess());
                GuiOpenWrapper.healthScreen().setHealthData(health);
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
        context.enqueueWork(() -> HealthLivingEntityList.loadServerData(data.tag()))
        .exceptionally(e -> {
            context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
            return null;
        });
    }
}
