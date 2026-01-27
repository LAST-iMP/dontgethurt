package com.lastimp.dgh.compact.touhoulittlemaid;

import com.lastimp.dgh.DontGetHurt;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetUp {
    @SubscribeEvent
    public static void onAttachCapabilities(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("touhou_little_maid")) return;
        MinecraftForge.EVENT_BUS.addListener(ModEventBus::onMaidTombstone);
        MinecraftForge.EVENT_BUS.addListener(ModEventBus::onMaidItemTransfer);
        MinecraftForge.EVENT_BUS.addListener(ModEventBus::onItemMaidTransfer);
    }
}
