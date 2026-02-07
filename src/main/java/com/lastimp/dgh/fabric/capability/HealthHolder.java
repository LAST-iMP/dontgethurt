package com.lastimp.dgh.fabric.capability;

import com.lastimp.dgh.fabric.capability.provider.HealthProvider;

public interface HealthHolder {
    HealthProvider dgh$getHealthProvider();
}
