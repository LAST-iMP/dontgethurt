package com.lastimp.dgh.fabric.capability;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.ICapabilityHelper;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CapabilityHelperNF implements ICapabilityHelper {
    @Override
    public boolean hasHealth(Entity entity) {
        if (entity instanceof Player player)
            return HealthLivingEntityList.isEntityWhitelisted(player.getType()) && !HealthLivingEntityList.isPlayerBlacklisted(player);
        return HealthLivingEntityList.isEntityWhitelisted(entity.getType());
    }

    @Override
    public Optional<HealthCapability> getHealth(LivingEntity entity) {
        if (!hasHealth(entity)) return Optional.empty();
        return Optional.of(((HealthHolder)entity).dgh$getHealthProvider().getCapability());
    }

    @Override
    public void saveHealth(LivingEntity entity) {
    }
}
