
package com.lastimp.dgh;

import com.lastimp.dgh.config.BlackList;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.config.HealthLivingEntityList;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.register.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

@Mod(DontGetHurt.MODID)
public class DontGetHurt
{
    public static final String MODID = "dgh";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final float DELTA = 0.05f;
    public static final float EPS = 0.0001f;

    public DontGetHurt() {
        this(FMLJavaModLoadingContext.get());
    }

    public DontGetHurt(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        init(modEventBus, context);
    }

    private void init(IEventBus modEventBus, FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenus.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeModTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModVillagers.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
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
        Network.registerMessage();
        event.enqueueWork(() -> {
            HealthLivingEntityList.loadWhiteLists();
            BlackList.loadExternalBlacklist();
            HealthLivingEntityList.loadExternallist();
        });
    }
}
