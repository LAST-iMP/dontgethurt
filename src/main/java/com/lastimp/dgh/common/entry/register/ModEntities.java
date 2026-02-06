package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entity.StretcherEntity;
import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class ModEntities {
    public static IEntry<EntityType<StretcherEntity>> STRETCHER = registerEntityType("stretcher", () -> StretcherEntity.TYPE);

    private static <T extends Entity> IEntry<EntityType<T>> registerEntityType(String name, Supplier<EntityType<?>> sup) {
        return PlatformService.REGISTRY_HANDLER.registerEntityType(name, sup);
    }

    public static void register() {
    }
}
