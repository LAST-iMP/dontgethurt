package com.lastimp.dgh.common.client;

import com.lastimp.dgh.common.client.hotkey.IKeyHelper;

import java.util.ServiceLoader;

public class ClientPlatformService {
    public static final IKeyHelper KEY_HELPER = ClientPlatformService.load(IKeyHelper.class);

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
