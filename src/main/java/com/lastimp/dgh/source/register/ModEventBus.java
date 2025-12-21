
package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;
import vazkii.patchouli.api.PatchouliAPI;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModCapabilities.register(event);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;

        var player = event.getEntity();

        var data = player.getPersistentData();
        var key = "dgh_new_player";

        if (!data.getBoolean(key)) {
            player.getInventory().add(new ItemStack(ModItems.HEALTH_CARE_BAG.get()));
            player.getInventory().add(new ItemStack(ModItems.BANDAGE.get(), 8));
            player.getInventory().add(new ItemStack(ModItems.MORPHINE.get(), 2));
            data.putBoolean(key, true);
        }
    }
}
