package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModCapabilities {
    public static final EntityCapability<HealthCapability, Void> PLAYER_HEALTH_HANDLER =
            EntityCapability.createVoid(
                    Common.ResourceLocation(DontGetHurt.MODID, "player_health_handler"),
                    HealthCapability.class
            );

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, DontGetHurt.MODID);

    public static final Supplier<AttachmentType<HealthCapability>> PLAYER_HEALTH =
            ATTACHMENT_TYPES.register("player_health", () -> AttachmentType.serializable(HealthCapability::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
