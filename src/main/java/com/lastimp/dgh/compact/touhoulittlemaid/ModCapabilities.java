package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.capability.HealthProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!ModList.get().isLoaded("touhou_little_maid")) return;
        if (event.getObject() instanceof EntityMaid) {
            event.addCapability(Common.ResourceLocation(DontGetHurt.MODID, "health"), new HealthProvider());
        }
        HealthProvider.add(EntityMaid.class);
    }
}
