package com.lastimp.dgh.fabric.event.callback;

import com.lastimp.dgh.fabric.event.HealthDamageEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface HealthDamageCallback {
    Event<HealthDamageCallback> EVENT = EventFactory.createArrayBacked(HealthDamageCallback.class,
            (listeners) -> (event) -> {
                for (HealthDamageCallback listener : listeners) {
                    event = listener.interact(event);
                }
                return event;
            });

    HealthDamageEvent interact(HealthDamageEvent event);
}
