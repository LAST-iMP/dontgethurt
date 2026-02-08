package com.lastimp.dgh.fabric.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public interface PlayerEventCallBack {
    interface BreakSpeed extends PlayerEventCallBack {
        Event<PlayerEventCallBack.BreakSpeed> EVENT = EventFactory.createArrayBacked(PlayerEventCallBack.BreakSpeed.class,
                (listeners) -> (player) -> {
                    float amp = 1;
                    for (PlayerEventCallBack.BreakSpeed listener : listeners) {
                        amp *= listener.interact(player);
                    }
                    return amp;
                });

        float interact(Player player);
    }
}
