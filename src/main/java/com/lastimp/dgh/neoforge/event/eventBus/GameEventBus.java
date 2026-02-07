package com.lastimp.dgh.neoforge.event.eventBus;

import com.lastimp.dgh.common.utils.command.Command;
import com.lastimp.dgh.common.entry.register.ModVillagers;
import com.lastimp.dgh.common.event.eventHandler.LivingEntityEventHandler;
import com.lastimp.dgh.common.event.eventHandler.PlayerEventHandler;
import com.lastimp.dgh.common.event.eventHandler.VillagerEventHandler;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber(modid = Utils.MODID)
public class GameEventBus {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Command.onRegisterCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        ModVillagers.addCustomTrades(event.getType(), event.getTrades());
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;
        LivingEntityEventHandler.addOrgan(livingEntity);

        if (!(livingEntity instanceof Villager villager)) return;
        VillagerEventHandler.addBrain(villager);
    }

    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEventHandler.logIn(event.getEntity());
    }

    @SubscribeEvent
    public static void logOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEventHandler.logOut(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
        var result = PlayerEventHandler.onPlayerInteractEntity(event.getEntity(), event.getTarget(), event.getHand());
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        PlayerEventHandler.onPlayerRespawn(event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        var entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity)) return;
        LivingEntityEventHandler.tickPre(livingEntity);
    }

    @SubscribeEvent
    public static void onBreath(LivingBreatheEvent event) {
        if (event.canBreathe())
            event.setCanBreathe(LivingEntityEventHandler.onBreath(event.getEntity()));
    }

    @SubscribeEvent
    public static void onInjury(LivingDamageEvent.Pre event) {
        var damage = LivingEntityEventHandler.onInjury(event.getEntity(), event.getSource(), event.getNewDamage());
        event.setNewDamage(damage);
    }

    @SubscribeEvent
    public static void onHealing(LivingHealEvent event) {
        LivingEntityEventHandler.onHealing(event.getEntity(), event.getAmount());
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntityEventHandler.onLivingDeath(event.getEntity());
    }
}
