
package com.lastimp.dgh.network;

import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPayloadHandler {
    private static HealthScreen healthScreen = null;

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MyReadAllConditionData.handlerClient(data, ctx)));
        context.setPacketHandled(true);
    }

    public static HealthScreen getHealthScreen() {
        return healthScreen;
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        ClientPayloadHandler.healthScreen = healthScreen;
    }
}
