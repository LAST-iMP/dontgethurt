package com.lastimp.dgh.fabric.event.eventBus;

import cn.sh1rocu.tacz.api.event.LivingEvent;
import com.lastimp.dgh.common.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractArm;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.common.utils.command.Command;
import com.lastimp.dgh.common.entry.register.ModVillagers;
import com.lastimp.dgh.common.event.eventHandler.LivingEntityEventHandler;
import com.lastimp.dgh.common.event.eventHandler.PlayerEventHandler;
import com.lastimp.dgh.common.event.eventHandler.VillagerEventHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.server.level.ServerPlayer;
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

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntityEventHandler.tickPre(event.getEntity());
    }

    @SubscribeEvent
    public static void onBreath(LivingBreatheEvent event) {
        event.setCanBreathe(LivingEntityEventHandler.onBreath(event.getEntity()));
    }

    @SubscribeEvent
    public static void onInjury(LivingDamageEvent event) {
        var damage = LivingEntityEventHandler.onInjury(event.getEntity(), event.getSource(), event.getAmount());
        event.setAmount(damage);
    }

    @SubscribeEvent
    public static void onHealing(LivingHealEvent event) {
        LivingEntityEventHandler.onHealing(event.getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntityEventHandler.onLivingDeath(event.getEntity());
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        float modifier = KeepLivingEffect.onBreakSpeed(event.getEntity());
        modifier *= AbstractArm.onBreakSpeed(event.getEntity());
        event.setNewSpeed(event.getOriginalSpeed() * modifier);
    }

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        int safeDistance = AbstractLeg.onFall(event.getEntity());
        event.setDistance(Math.max(0, event.getDistance() - safeDistance));
    }

    public static void init() {

    }
}
