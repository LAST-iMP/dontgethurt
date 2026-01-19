package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID, value = Dist.CLIENT)
public class ClientTickEventHandler {
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (HealthCapability.isDying(event.getEntity()))
            event.getEntity().setPose(Pose.SWIMMING);
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        if (HealthCapability.isDying(event.getEntity()))
            event.getEntity().setPose(Pose.SWIMMING);
    }
}
