package com.lastimp.dgh.common;

import com.lastimp.dgh.common.capability.ICapabilityHelper;
import com.lastimp.dgh.common.config.IConfig;
import com.lastimp.dgh.common.container.IBackpackFactory;
import com.lastimp.dgh.common.entry.IRegistryHandler;
import com.lastimp.dgh.common.event.IEventHook;
import com.lastimp.dgh.common.network.INetwork;

import java.util.ServiceLoader;

public class PlatformService {
    public static final INetwork NETWORK = PlatformService.load(INetwork.class);
    public static final IRegistryHandler REGISTRY_HANDLER = PlatformService.load(IRegistryHandler.class);
    public static final IEventHook EVENT_HOOK = PlatformService.load(IEventHook.class);
    public static final IConfig CONFIG = PlatformService.load(IConfig.class);
    public static final IBackpackFactory BACKPACK_FACTORY = PlatformService.load(IBackpackFactory.class);
    public static final ICapabilityHelper CAPABILITY_HELPER = PlatformService.load(ICapabilityHelper.class);

    private static <T> T load(Class<T> service) {
        return ServiceLoader.load(service)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "No service implementation found for " + service.getName()
                        )
                );
    }
}
