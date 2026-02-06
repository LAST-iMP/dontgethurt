
package com.lastimp.dgh.fabric;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.fabric.config.ConfigNF;
import com.lastimp.dgh.fabric.network.ModNetwork;
import com.lastimp.dgh.fabric.entry.RegistryHandlerNF;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public class DontGetHurt {

    @SuppressWarnings("removal")
    public DontGetHurt() {
        this(FMLJavaModLoadingContext.get());
    }

    public DontGetHurt(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        init(modEventBus, context);
    }

    private void init(IEventBus modEventBus, FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, ConfigNF.SPEC);

        PlatformService.REGISTRY_HANDLER.register();

        RegistryHandlerNF.BLOCKS.register(modEventBus);
        RegistryHandlerNF.BLOCK_ENTITIES.register(modEventBus);
        RegistryHandlerNF.ITEMS.register(modEventBus);
        RegistryHandlerNF.MENU_TYPES.register(modEventBus);
        RegistryHandlerNF.MOB_EFFECTS.register(modEventBus);
        RegistryHandlerNF.MOD_POTIONS.register(modEventBus);
        RegistryHandlerNF.SOUNDS.register(modEventBus);
        RegistryHandlerNF.CREATIVE_MODE_TABS.register(modEventBus);
        RegistryHandlerNF.ENTITY_TYPES.register(modEventBus);
        RegistryHandlerNF.POI_TYPES.register(modEventBus);
        RegistryHandlerNF.VILLAGER_PROFESSION.register(modEventBus);

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
        Utils.LOGGER.info("HELLO FROM COMMON SETUP");
        ModNetwork.registerMessage();
        event.enqueueWork(() -> {
            HealthLivingEntityList.loadExternallist();
            ConditionAccessor.init();
        });
    }
}
