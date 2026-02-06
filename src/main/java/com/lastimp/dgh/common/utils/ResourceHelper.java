package com.lastimp.dgh.common.utils;

import net.minecraft.resources.ResourceLocation;

public class ResourceHelper {
    public static ResourceLocation ModResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(Utils.MODID, path);
    }

    public static ResourceLocation ResourceLocation(String path) {
        return ResourceLocation.parse(path);
    }

    public static ResourceLocation ResourceLocation(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation ResourceBySeperator(String path, char seperator) {
        return ResourceLocation.bySeparator(path, seperator);
    }
}
