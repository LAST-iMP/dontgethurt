
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.config.HealthLivingEntityList;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilityProvider<Entity, Void, HealthCapability>, INBTSerializable<CompoundTag> {
    private final HealthCapability impl = new HealthCapability();

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return impl.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        impl.deserializeNBT(provider, nbt);
    }

    @Override
    public @Nullable HealthCapability getCapability(Entity o, Void unused) {
        if (has(o)) return this.impl;
        return null;
    }

    public static boolean has(Entity entity) {
        if (entity instanceof Player player)
            return HealthLivingEntityList.isEntityWhitelisted(player.getType()) && !HealthLivingEntityList.isPlayerBlacklisted(player);
        return HealthLivingEntityList.isEntityWhitelisted(entity.getType());
    }
}