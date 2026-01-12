package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.HealthLivingEntityList;
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
        HealthLivingEntityList.addWhiteList(EntityMaid.TYPE);
        NeoForge.EVENT_BUS.addListener(ModEventBus::onMaidTombstone);
    }
}
