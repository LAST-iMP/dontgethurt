package com.lastimp.dgh.fabric.event.callback;

import com.lastimp.dgh.fabric.event.ComponentDamageEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface ComponentDamageCallback {
    Event<ComponentDamageCallback> EVENT = EventFactory.createArrayBacked(ComponentDamageCallback.class,
            (listeners) -> (event) -> {
                for (ComponentDamageCallback listener : listeners) {
                    event = listener.interact(event);
                }
                return event;
            });

    ComponentDamageEvent interact(ComponentDamageEvent event);
}
