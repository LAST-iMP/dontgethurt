package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (!ModList.get().isLoaded("touhou_little_maid")) return;
        event.registerEntity(
                com.lastimp.dgh.source.register.ModCapabilities.HEALTH_HANDLER,
                EntityMaid.TYPE,
                new HealthProvider()
        );
        HealthProvider.add(EntityMaid.class);
    }
}
