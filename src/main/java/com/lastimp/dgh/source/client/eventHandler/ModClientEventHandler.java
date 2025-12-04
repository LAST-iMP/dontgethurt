
package com.lastimp.dgh.source.client.eventHandler;

import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        for (var key : KeyBinding.keys)
            event.register(key);
    }
}
