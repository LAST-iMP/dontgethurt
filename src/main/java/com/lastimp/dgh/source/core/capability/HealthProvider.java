
package com.lastimp.dgh.source.core.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class HealthProvider implements ICapabilityProvider<LivingEntity, Void, HealthCapability>, INBTSerializable<CompoundTag> {
    private final HealthCapability impl = new HealthCapability();
    private static final Set<Class<? extends LivingEntity>> availClasses = new HashSet<>();

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

    public static <T extends LivingEntity> void add(Class<T> entity) {
        availClasses.add(entity);
    }

    public static <T extends LivingEntity> boolean has(LivingEntity entity) {
        return availClasses.contains(entity.getClass());
    }
}