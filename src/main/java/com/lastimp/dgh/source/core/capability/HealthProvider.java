
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.source.register.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class HealthProvider implements ICapabilitySerializable<CompoundTag> {
    private final HealthCapability impl = new HealthCapability();
    private final LazyOptional<HealthCapability> optional = LazyOptional.of(() -> impl);
    private static final Set<Class<? extends LivingEntity>> availClasses = new HashSet<>();

    @Override
    public CompoundTag serializeNBT() {
        return impl.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        impl.deserializeNBT(nbt);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ModCapabilities.HEALTH) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    public static <T extends LivingEntity> void add(Class<T> entity) {
        availClasses.add(entity);
    }

    public static <T extends LivingEntity> boolean has(LivingEntity entity) {
        for (Class<? extends LivingEntity> testClass : availClasses) {
            if (testClass.isInstance(entity)) return true;
        }
        return false;
    }
}