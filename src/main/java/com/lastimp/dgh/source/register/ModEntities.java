package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.entity.StretcherEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static DeferredHolder<EntityType<?>, EntityType<StretcherEntity>> STRETCHER;

    static {
        ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, DontGetHurt.MODID);
        STRETCHER = ENTITY_TYPES.register("stretcher", () -> StretcherEntity.TYPE);
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
