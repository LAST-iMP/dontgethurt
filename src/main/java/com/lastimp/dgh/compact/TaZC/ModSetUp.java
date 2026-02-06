package com.lastimp.dgh.compact.TaZC;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import com.lastimp.dgh.common.utils.Utils;

@EventBusSubscriber(modid = Utils.MODID)
public class ModSetUp {
    @SubscribeEvent
    public static void onAttachCapabilities(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("tazc")) return;
    }
}