package com.lastimp.dgh.neoforge.entry.register;

import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.neoforge.capability.HealthCapabilityNF;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModCapabilities {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Utils.MODID);

    public static final Supplier<AttachmentType<? extends HealthCapability>> HEALTH =
            ATTACHMENT_TYPES.register("player_health", () -> AttachmentType.serializable(HealthCapabilityNF::new).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
