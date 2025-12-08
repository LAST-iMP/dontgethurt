package com.lastimp.dgh.neoforge;

import net.minecraft.resources.ResourceLocation;

public class Common {
    public static ResourceLocation ResourceLocation(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation ResourceBySeperator(String path, char seperator) {
        return ResourceLocation.bySeparator(path, seperator);
    }
}
