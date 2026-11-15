package com.lastimp.dgh.api.client.impl;

import com.lastimp.dgh.api.client.IHealthDataAPI;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.client.ClientAccessor;
import com.lastimp.dgh.common.network.message.MyReadAllConditionData;
import net.minecraft.nbt.CompoundTag;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static com.lastimp.dgh.common.enums.OperationType.HEALTH_SCANN;

public class HealthDataAPI implements IHealthDataAPI {
    private static final Map<UUID, HealthCapability> healthDisplayData = new HashMap<>();
    private static final Map<UUID, Map<String, Consumer<HealthCapability>>> callBackWhenUpdated = new HashMap<>();

    public void beginCallingDataFromServer(UUID uuid, List<Pair<String, Consumer<HealthCapability>>> callbacks) {
        check(uuid);
        var callbackMap = callBackWhenUpdated.get(uuid);
        for (var callback : callbacks) {
            callbackMap.put(callback.getA(), callback.getB());
        }
    }

    public void callForHealthDataFromServer(UUID uuid) {
        if (!healthDisplayData.containsKey(uuid)) return;
        PlatformService.NETWORK.sendToServer(MyReadAllConditionData.getInstance(
                uuid, 0, new CompoundTag(), HEALTH_SCANN
        ));
    }

    public void endCallingDataFromServer(UUID uuid, List<String> callbacks) {
        check(uuid);
        var callbackMap = callBackWhenUpdated.get(uuid);
        for (var callback : callbacks) {
            callbackMap.remove(callback);
        }
    }

    public void updateHealthDisplayData(UUID uuid, CompoundTag tag) {
        check(uuid);
        var data = healthDisplayData.get(uuid);
        data.deserialize(ClientAccessor.getPlayerOrThrow().registryAccess(), tag);
        callBackWhenUpdated.get(uuid).values().forEach(callback -> callback.accept(data));
    }

    private static void check(UUID uuid) {
        if (!healthDisplayData.containsKey(uuid)) {
            healthDisplayData.put(uuid, new HealthCapability());
            callBackWhenUpdated.put(uuid, new HashMap<>());
        }
    }
}
