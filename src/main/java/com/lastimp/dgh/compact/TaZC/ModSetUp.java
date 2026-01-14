package com.lastimp.dgh.compact.TaZC;

import com.lastimp.dgh.DontGetHurt;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModSetUp {
    @SubscribeEvent
    public static void onAttachCapabilities(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("tazc")) return;
    }
}