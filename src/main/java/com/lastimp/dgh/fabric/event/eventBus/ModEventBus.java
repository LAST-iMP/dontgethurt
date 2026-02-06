package com.lastimp.dgh.fabric.event.eventBus;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.fabric.container.BackpackInventoryNF;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Utils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(HealthCapability.class);
        event.register(BackpackInventoryNF.class);
    }
}