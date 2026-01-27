package com.lastimp.dgh.compact.touhoulittlemaid;

import com.lastimp.dgh.DontGetHurt;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModSetUp {
    @SubscribeEvent
    public static void onAttachCapabilities(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("touhou_little_maid")) return;
        NeoForge.EVENT_BUS.addListener(ModEventBus::onMaidTombstone);
        NeoForge.EVENT_BUS.addListener(ModEventBus::onMaidItemTransfer);
        NeoForge.EVENT_BUS.addListener(ModEventBus::onItemMaidTransfer);
    }
}
