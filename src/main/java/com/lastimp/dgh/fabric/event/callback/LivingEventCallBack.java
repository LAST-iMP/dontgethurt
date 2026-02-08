package com.lastimp.dgh.fabric.event.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEventCallBack {

    interface LivingTickEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.LivingTickEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.LivingTickEvent.class,
                (listeners) -> (livingEntity) -> {
                    for (LivingEventCallBack.LivingTickEvent listener : listeners) {
                        listener.interact(livingEntity);
                    }
                });

        void interact(LivingEntity livingEntity);
    }

    interface BreathEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.BreathEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.BreathEvent.class,
                (listeners) -> (livingEntity) -> {
                    boolean canBreath = true;
                    for (LivingEventCallBack.BreathEvent listener : listeners) {
                        canBreath &= listener.interact(livingEntity);
                    }
                    return canBreath;
                });

        boolean interact(LivingEntity livingEntity);
    }

    interface DamageEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.DamageEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.DamageEvent.class,
                (listeners) -> (livingEntity, damageSource, damage) -> {
                    for (LivingEventCallBack.DamageEvent listener : listeners) {
                        damage = listener.interact(livingEntity, damageSource, damage);
                    }
                    return damage;
                });

        float interact(LivingEntity livingEntity, DamageSource source, float damage);
    }

    interface HealingEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.HealingEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.HealingEvent.class,
                (listeners) -> (livingEntity, amount) -> {
                    for (LivingEventCallBack.HealingEvent listener : listeners) {
                        listener.interact(livingEntity, amount);
                    }
                });

        void interact(LivingEntity livingEntity, float amount);
    }

    interface DeathEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.DeathEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.DeathEvent.class,
                (listeners) -> (livingEntity) -> {
                    for (LivingEventCallBack.DeathEvent listener : listeners) {
                        listener.interact(livingEntity);
                    }
                });

        void interact(LivingEntity livingEntity);
    }

    interface FallEvent extends LivingEventCallBack {
        Event<LivingEventCallBack.FallEvent> EVENT = EventFactory.createArrayBacked(LivingEventCallBack.FallEvent.class,
                (listeners) -> (livingEntity) -> {
                    float safeDist = 0;
                    for (LivingEventCallBack.FallEvent listener : listeners) {
                        safeDist +=listener.interact(livingEntity);
                    }
                    return safeDist;
                });

        float interact(LivingEntity livingEntity);
    }
}
