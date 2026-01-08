package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.config.HealthLivingEntityList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {
    @SubscribeEvent
    public static void onAttachCapabilities(final FMLCommonSetupEvent event) {
        if (!ModList.get().isLoaded("touhou_little_maid")) return;
        HealthLivingEntityList.add(EntityMaid.TYPE);
    }
}
