
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.message.MyServerConfigSynData;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class ClientPayloadHandler {

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            OperationType operation = OperationType.valueOf(data.oper());
            if (operation == OperationType.HEALTH_SCANN && ClientAccessor.healthScreen() != null) {
                HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag());
                ClientAccessor.healthScreen().setHealthData(health);
            } else if (operation == OperationType.SYN) {
                var entity = ClientAccessor.getLiving(data.entityID());
                if (entity != null && HealthCapability.has(entity)) {
                    HealthCapability.getAndApply(entity, health -> health.lightDeserializeNBT(data.tag()));
                }
            }
        }));
        context.setPacketHandled(true);
    }

    public static void handleServerConfigSYNData(final MyServerConfigSynData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> HealthLivingEntityList.loadServerData(data.healthWhiteList()));
        context.setPacketHandled(true);
    }
}
