package com.lastimp.dgh.fabric.event.eventBus;

import com.lastimp.dgh.common.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractArm;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractLeg;
import com.lastimp.dgh.common.utils.command.Command;
import com.lastimp.dgh.common.entry.register.ModBlocks;
import com.lastimp.dgh.common.entry.register.ModVillagers;
import com.lastimp.dgh.common.event.eventHandler.LivingEntityEventHandler;
import com.lastimp.dgh.common.event.eventHandler.PlayerEventHandler;
import com.lastimp.dgh.common.event.eventHandler.VillagerEventHandler;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.fabric.capability.provider.BagItemInventoryProvider;
import com.lastimp.dgh.fabric.capability.provider.HealthProvider;
import com.lastimp.dgh.fabric.container.BackpackInventoryNF;
import com.lastimp.dgh.fabric.entry.register.ModCapabilities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Utils.MODID)
public class GameEventBus {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        Command.onRegisterCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onHealthAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity livingEntity && HealthCapability.has(livingEntity)) {
            event.addCapability(ModCapabilities.HEALTH_RL, new HealthProvider());
        }
    }

    @SubscribeEvent
    public static void onBagAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        var stack = event.getObject();
        if (stack.getItem() instanceof AbstractSmallBag bag) {
            var inv = new BackpackInventoryNF(9);
            var provider = new BagItemInventoryProvider(inv, stack);
            bag.initBag(inv);
            event.addCapability(ModCapabilities.BAG_INV_RL, provider);
        }
    }

    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        event.register(
                Registries.POINT_OF_INTEREST_TYPE,
                helper -> {
                    for (var state : ModBlocks.OPERATING_BED_BLOCK.get().getStateDefinition().getPossibleStates()) {
                        GameData.getBlockStatePointOfInterestTypeMap().put(state, ModVillagers.DOCTOR_POI.get());
                    }
                }
        );
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
}
