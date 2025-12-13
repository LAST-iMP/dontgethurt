
package com.lastimp.dgh.source.core.player;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class PlayerHealthProvider implements ICapabilityProvider<LivingEntity, Void, PlayerHealthCapability>, INBTSerializable<CompoundTag> {
    private final PlayerHealthCapability impl = new PlayerHealthCapability();

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return impl.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        impl.deserializeNBT(provider, nbt);
    }

    @Override
    public @Nullable PlayerHealthCapability getCapability(LivingEntity o, Void unused) {
        return this.impl;
    }
}