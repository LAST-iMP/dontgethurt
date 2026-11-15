package com.lastimp.dgh.api.client;

import com.lastimp.dgh.api.client.impl.HealthDataAPI;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.nbt.CompoundTag;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class DGH_ClientAPI {
    private static final IHealthDataAPI healthData = new HealthDataAPI();

    public static void beginCallingDataFromServer(UUID uuid, List<Pair<String, Consumer<HealthCapability>>> callbacks) {
        healthData.beginCallingDataFromServer(uuid, callbacks);
    }

    public static void callForHealthDataFromServer(UUID uuid) {
        healthData.callForHealthDataFromServer(uuid);
    }

    public static void endCallingDataFromServer(UUID uuid, List<String> callbacks) {
        healthData.endCallingDataFromServer(uuid, callbacks);
    }

    public static void updateHealthDisplayData(UUID uuid, CompoundTag tag) {
        healthData.updateHealthDisplayData(uuid, tag);
    }
}
