
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.client.ClientAccessor;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPayloadHandler {

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag());
            OperationType operation = OperationType.valueOf(data.oper());
            if (operation == OperationType.HEALTH_SCANN && ClientAccessor.healthScreen() != null) {
                ClientAccessor.healthScreen().setHealthData(health);
            } else if (operation == OperationType.SYN) {
                ClientAccessor.setHealth(health);
            }
        }));
        context.setPacketHandled(true);
    }
}
