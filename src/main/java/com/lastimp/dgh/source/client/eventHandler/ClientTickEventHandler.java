package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(value = Dist.CLIENT)
@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ClientTickEventHandler {
    @SubscribeEvent
    public static void onInputTick(TickEvent.PlayerTickEvent event) {
        if (HealthCapability.isDying(event.player))
            event.player.setPose(Pose.SWIMMING);
    }
}
