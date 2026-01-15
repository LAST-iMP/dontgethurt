
package com.lastimp.dgh;

import com.lastimp.dgh.config.BlackList;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.source.register.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(DontGetHurt.MODID)
public class DontGetHurt
{
    public static final String MODID = "dgh";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final float DELTA = 0.05f;
    public static final float EPS = 0.0001f;

    public DontGetHurt(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenus.register(modEventBus);
        ModCapabilities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModVillagers.register(modEventBus);

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
        LOGGER.info("HELLO FROM COMMON SETUP");
        event.enqueueWork(() -> {
            BlackList.loadExternalBlacklist();
            HealthLivingEntityList.loadExternallist();
        });
    }
}
