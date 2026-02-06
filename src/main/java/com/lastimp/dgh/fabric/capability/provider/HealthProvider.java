package com.lastimp.dgh.fabric.capability.provider;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.fabric.entry.register.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilitySerializable<CompoundTag> {
    private final HealthCapability impl = new HealthCapability();
    private final LazyOptional<HealthCapability> optional = LazyOptional.of(() -> impl);

    @Override
    public CompoundTag serializeNBT() {
        return impl.serialize();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        impl.deserialize(nbt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.HEALTH) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }
}