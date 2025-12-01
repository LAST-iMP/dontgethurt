
package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataPackRegistryEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBus {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModCapabilities.register(event);
    }

//    @SubscribeEvent
//    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
//        event.dataPackRegistry(
//                Registries.DAMAGE_TYPE,
//                DamageType.CODEC,
//                DamageType.CODEC
//        );
//    }
}
