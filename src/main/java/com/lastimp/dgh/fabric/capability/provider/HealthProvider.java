package com.lastimp.dgh.fabric.capability.provider;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.utils.Lazy;
import com.lastimp.dgh.common.utils.Serializable;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class HealthProvider implements Serializable {
    private final HealthCapability impl = new HealthCapability();
    private final Lazy<HealthCapability> optional = Lazy.of(() -> impl);

    @Override
    public CompoundTag serialize() {
        return impl.serialize();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        impl.deserialize(nbt);
    }

    public @NotNull HealthCapability getCapability() {
        return optional.get();
    }
}