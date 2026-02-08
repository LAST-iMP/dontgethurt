package com.lastimp.dgh.fabric.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;

public interface ScreenCallback {
    interface Opening extends ScreenCallback {
        Event<Opening> EVENT = EventFactory.createArrayBacked(Opening.class,
                (listeners) -> (newScreen, currentScreen) -> {
                    boolean success = true;
                    for (Opening listener : listeners) {
                        success &= listener.interact(newScreen, currentScreen);
                    }
                    return success;
                });

        boolean interact(Screen newScreen, Screen currentScreen);
    }
}
