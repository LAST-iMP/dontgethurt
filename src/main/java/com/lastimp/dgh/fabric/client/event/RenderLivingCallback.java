package com.lastimp.dgh.fabric.client.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public interface RenderLivingCallback {
    interface Pre extends RenderLivingCallback {
        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (entity, renderer) -> {
                    for (Pre listener : listeners) {
                        listener.interact(entity, renderer);
                    }
                });

        void interact(LivingEntity livingEntity, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer);
    }

    interface Post extends RenderLivingCallback {
        Event<Pre> EVENT = EventFactory.createArrayBacked(Pre.class,
                (listeners) -> (entity, renderer) -> {
                    for (Pre listener : listeners) {
                        listener.interact(entity, renderer);
                    }
                });

        void interact(LivingEntity livingEntity, LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer);
    }
}
