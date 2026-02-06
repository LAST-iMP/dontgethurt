package com.lastimp.dgh.common.capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public interface ICapabilityHelper {
    boolean hasHealth(Entity entity);
    Optional<HealthCapability> getHealth(LivingEntity entity);
    void saveHealth(LivingEntity entity);
}
