package com.lastimp.dgh.fabric.event.eventBus;

import com.lastimp.dgh.common.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.common.utils.command.Command;
import com.lastimp.dgh.common.entry.register.ModVillagers;
import com.lastimp.dgh.common.event.eventHandler.LivingEntityEventHandler;
import com.lastimp.dgh.common.event.eventHandler.PlayerEventHandler;
import com.lastimp.dgh.common.event.eventHandler.VillagerEventHandler;
import com.lastimp.dgh.fabric.event.callback.LivingEventCallBack;
import com.lastimp.dgh.fabric.event.callback.PlayerEventCallBack;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.LinkedList;
import java.util.List;

import static com.lastimp.dgh.common.entry.register.ModVillagers.DOCTOR_MAKER;

public class GameEventBus {
    public static void onRegisterCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
            Command.onRegisterCommands(dispatcher);
        });
    }

    public static void addCustomTrades() {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = new Int2ObjectArrayMap<>();
        for (int i = 1; i <= 5; i++) {
            trades.put(i, new LinkedList<>());
        };
        var tradesList = ModVillagers.addCustomTrades(DOCTOR_MAKER.get(), trades);
        for (int i = 1; i <= 5; i++) {
            var levelTrade = tradesList.get(i);
            TradeOfferHelper.registerVillagerOffers(DOCTOR_MAKER.get(), i, list -> list.addAll(levelTrade));
        };
    }

    public static void onEntityJoin() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {

            if (!(entity instanceof LivingEntity livingEntity)) return;

            LivingEntityEventHandler.addOrgan(livingEntity);

            if (!(livingEntity instanceof Villager villager)) return;
            VillagerEventHandler.addBrain(villager);
        });
    }

    public static void logIn() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerEventHandler.logIn(handler.player);
        });
    }

    public static void logOut() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            PlayerEventHandler.logOut(handler.player);
        });
    }

    public static void onPlayerInteractEntity() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            var result = PlayerEventHandler.onPlayerInteractEntity(player, entity, hand);

            if (result.consumesAction()) {
                return result;
            }
            return InteractionResult.PASS;
        });
    }

    public static void onPlayerRespawn() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {

            PlayerEventHandler.onPlayerRespawn(newPlayer);
        });
    }

    public static void onEntityTick() {
        LivingEventCallBack.LivingTickEvent.EVENT.register(LivingEntityEventHandler::tickPre);
    }

    public static void onBreath() {
        LivingEventCallBack.BreathEvent.EVENT.register(LivingEntityEventHandler::onBreath);
    }

    public static void onInjury() {
        LivingEventCallBack.DamageEvent.EVENT.register(LivingEntityEventHandler::onInjury);
    }

    public static void onHealing() {
        LivingEventCallBack.HealingEvent.EVENT.register(LivingEntityEventHandler::onHealing);
    }

    public static void onLivingDeath() {
        LivingEventCallBack.DeathEvent.EVENT.register(LivingEntityEventHandler::onLivingDeath);
    }

    public static void onBreakSpeed() {
        PlayerEventCallBack.BreakSpeed.EVENT.register(KeepLivingEffect::onBreakSpeed);
    }

    public static void onFall() {
        LivingEventCallBack.FallEvent.EVENT.register(AbstractLeg::onFall);
    }

    public static void init() {
        onRegisterCommands();
        addCustomTrades();
        onEntityJoin();
        logIn();
        logOut();
        onPlayerInteractEntity();
        onPlayerRespawn();
        onEntityTick();
        onBreath();
        onInjury();
        onHealing();
        onLivingDeath();
        onBreakSpeed();
        onFall();
    }
}
