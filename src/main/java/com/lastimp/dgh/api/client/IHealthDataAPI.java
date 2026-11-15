package com.lastimp.dgh.api.client;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.nbt.CompoundTag;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface IHealthDataAPI {
    void beginCallingDataFromServer(UUID uuid, List<Pair<String, Consumer<HealthCapability>>> callbacks);

    void callForHealthDataFromServer(UUID uuid);

    void endCallingDataFromServer(UUID uuid, List<String> callbacks);

    void updateHealthDisplayData(UUID uuid, CompoundTag tag);
}
