
package com.lastimp.dgh.network;

import com.lastimp.dgh.source.client.gui.screen.HealthScreen;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.BloodScanner;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ClientPayloadHandler {
    private static HealthScreen healthScreen = null;
    private static HealthCapability health = null;

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    HealthCapability health = MyReadAllConditionData.getHealthFromInstance(data.tag(), context.player().registryAccess());
                    OperationType operation = OperationType.valueOf(data.oper());
                    if (operation == OperationType.HEALTH_SCANN && healthScreen != null) {
                        healthScreen.setHealthData(health);
                    } else if (operation == OperationType.BLOOD_SCANN) {
                        UUID uuid = new UUID(data.id_most(), data.id_least());
                        BloodScanner.scanHealth(context.player(), health, context.player().level().getPlayerByUUID(uuid).getScoreboardName());
                    } else if (operation == OperationType.SYN) {
                        ClientPayloadHandler.setHealth(health);
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public static HealthScreen getHealthScreen() {
        return healthScreen;
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
