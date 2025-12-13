
package com.lastimp.dgh.source.core.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilityProvider<LivingEntity, Void, HealthCapability>, INBTSerializable<CompoundTag> {
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
    public @Nullable HealthCapability getCapability(LivingEntity o, Void unused) {
        return this.impl;
    }
}