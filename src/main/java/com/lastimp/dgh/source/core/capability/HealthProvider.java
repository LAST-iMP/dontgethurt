
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.register.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilitySerializable<CompoundTag> {
    private final HealthCapability impl = new HealthCapability();
    private final LazyOptional<HealthCapability> optional = LazyOptional.of(() -> impl);

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

    public static boolean has(Entity entity) {
        if (entity instanceof Player player)
            return HealthLivingEntityList.isEntityWhitelisted(player.getType()) && !HealthLivingEntityList.isPlayerBlacklisted(player);
        return HealthLivingEntityList.isEntityWhitelisted(entity.getType());
    }
}