
package com.lastimp.dgh.neoforge;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.config.ModConfigs;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.neoforge.config.ConfigNF;

import com.lastimp.dgh.neoforge.entry.RegistryHandlerNF;
import com.lastimp.dgh.neoforge.entry.register.ModCapabilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Utils.MODID)
public class DontGetHurt {

    public DontGetHurt(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ConfigNF.SPEC);

        PlatformService.REGISTRY_HANDLER.register();

        RegistryHandlerNF.BLOCKS.register(modEventBus);
        RegistryHandlerNF.BLOCK_ENTITIES.register(modEventBus);
        RegistryHandlerNF.ITEMS.register(modEventBus);
        RegistryHandlerNF.MENU_TYPES.register(modEventBus);
        ModCapabilities.register(modEventBus);
        RegistryHandlerNF.MOB_EFFECTS.register(modEventBus);
        RegistryHandlerNF.MOD_POTIONS.register(modEventBus);
        RegistryHandlerNF.SOUNDS.register(modEventBus);
        RegistryHandlerNF.CREATIVE_MODE_TABS.register(modEventBus);
        RegistryHandlerNF.ENTITY_TYPES.register(modEventBus);
        RegistryHandlerNF.POI_TYPES.register(modEventBus);
        RegistryHandlerNF.VILLAGER_PROFESSION.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        server.getAllLevels().forEach(level -> {
            GameRules gameRules = level.getGameRules();
            gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        Utils.LOGGER.info("HELLO FROM COMMON SETUP");
        event.enqueueWork(() -> {
            ModConfigs.loadExternalList();
            ConditionAccessor.init();
        });
    }
}
