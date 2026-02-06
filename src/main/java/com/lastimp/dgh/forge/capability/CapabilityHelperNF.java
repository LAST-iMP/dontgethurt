package com.lastimp.dgh.forge.capability;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.ICapabilityHelper;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.forge.entry.register.ModCapabilities;
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
        return entity.getCapability(ModCapabilities.HEALTH, null).resolve();
    }

    @Override
    public void saveHealth(LivingEntity entity) {
    }
}
