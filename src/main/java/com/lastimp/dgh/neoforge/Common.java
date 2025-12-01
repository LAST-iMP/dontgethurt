package com.lastimp.dgh.neoforge;

import net.minecraft.resources.ResourceLocation;

public class Common {
    public static ResourceLocation ResourceLocation(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
