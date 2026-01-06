package com.lastimp.dgh.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class HealthLivingEntityList {
    public static final String ID = "health_white_list";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<EntityType<?>> HEALTH_WHITE_LIST = new HashSet<>();
    private static final ResourceLocation HEALTH_WHITE_LIST_JSON = Common.ResourceLocation(DontGetHurt.MODID, ID + ".json");

    public static boolean isEntityWhitelisted(EntityType<?> entityType) {
        return HEALTH_WHITE_LIST.contains(entityType);
    }

    public static Set<EntityType<?>> getList() {
        return new HashSet<>(HEALTH_WHITE_LIST);
    }

    public static void add(EntityType<?> entityType) {
        HEALTH_WHITE_LIST.add(entityType);
    }

    public static void loadWhiteLists(ResourceManager resourceManager) {
        try {
            Resource resource = resourceManager.getResource(HEALTH_WHITE_LIST_JSON).orElseThrow();
            load(new InputStreamReader(resource.open()));

            DontGetHurt.LOGGER.info("Loaded health whitelist: " + HEALTH_WHITE_LIST);
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load health whitelist JSON", e);
        }
    }

    public static void loadExternalWhitelist() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + ID + ".json");

        try {
            if (Files.exists(configPath)) {
                load(Files.newBufferedReader(configPath));
            } else {
                var writer = Files.newBufferedWriter(Files.createFile(configPath));
                writer.write("[]");
                writer.close();
            }
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load external health whitelist", e);
        }
    }

    private static void load(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        parseHealthWhitelist(root);
    }

    private static void parseHealthWhitelist(JsonArray root) {
        root.forEach(jsonElement -> {
            String entityId = jsonElement.getAsString();
            EntityType.byString(entityId).ifPresent(HEALTH_WHITE_LIST::add);
        });
    }
}
