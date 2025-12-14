
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    private static HealthScreen healthScreen = null;
    private static HealthCapability health;

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag(), context.player().registryAccess());
                    OperationType operation = OperationType.valueOf(data.oper());
                    if (operation == OperationType.HEALTH_SCANN && healthScreen != null) {
                        healthScreen.setHealthData(health);
                    } else if (operation == OperationType.SYN) {
                        ClientPayloadHandler.setHealth(health);
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public static void setHealthScreen(HealthScreen healthScreen) {
        ClientPayloadHandler.healthScreen = healthScreen;
    }

    public static HealthCapability health() {
        return health;
    }

    public static void setHealth(HealthCapability health) {
        ClientPayloadHandler.health = health;
    }
}
