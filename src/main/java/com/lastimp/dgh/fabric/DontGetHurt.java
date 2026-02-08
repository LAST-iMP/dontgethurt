
package com.lastimp.dgh.fabric;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.config.HealthLivingEntityList;
import com.lastimp.dgh.fabric.config.ConfigNF;
import com.lastimp.dgh.fabric.event.eventBus.GameEventBus;
import com.lastimp.dgh.fabric.network.ModNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.level.GameRules;

public class DontGetHurt implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigNF.init();
        GameEventBus.init();

        PlatformService.REGISTRY_HANDLER.register();

        ModNetwork.registerMessage();
        HealthLivingEntityList.loadExternallist();
        ConditionAccessor.init();

        onServerStarted();
    }

    public void onServerStarted() {
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> server.getAllLevels().forEach(level -> {
            GameRules gameRules = level.getGameRules();
            gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);
        }));
    }
}
