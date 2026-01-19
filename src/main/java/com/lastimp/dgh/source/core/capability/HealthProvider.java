
package com.lastimp.dgh.source.core.capability;

import com.lastimp.dgh.config.HealthLivingEntityList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.Nullable;

public class HealthProvider implements ICapabilityProvider<Entity, Void, HealthCapability>, ValueIOSerializable {
    private final HealthCapability impl = new HealthCapability();

    @Override
    public void serialize(ValueOutput output) {
        impl.serialize(output);
    }

    @Override
    public void deserialize(ValueInput input) {
        impl.deserialize(input);
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