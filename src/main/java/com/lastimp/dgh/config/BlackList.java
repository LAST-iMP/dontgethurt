package com.lastimp.dgh.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class BlackList {
    public static final String PULSE_EFFECT = "pulse_effect";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Set<EntityType<?>>> EFFECT_BLACKLISTS = new HashMap<>();
    private static final Identifier BLACKLIST_JSON = Common.getId(DontGetHurt.MODID, "blacklist.json");

    public static void loadBlacklists(ResourceManager resourceManager) {
        EFFECT_BLACKLISTS.clear();
        try {
            Resource resource = resourceManager.getResource(BLACKLIST_JSON).orElseThrow();
            load(new InputStreamReader(resource.open()));

            DontGetHurt.LOGGER.info("Loaded effect blacklists: " + EFFECT_BLACKLISTS.size());
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load effect blacklist JSON", e);
        }
    }
    private static void parseBlacklist(JsonObject root, String effectKey) {
        Set<EntityType<?>> blacklist = new HashSet<>();
        if (root.has(effectKey)) {
            root.getAsJsonArray(effectKey).forEach(jsonElement -> {
                String entityId = jsonElement.getAsString();
                EntityType.byString(entityId).ifPresent(blacklist::add);
            });
        }
        EFFECT_BLACKLISTS.put(effectKey, blacklist);
    }

    public static boolean isEntityBlacklisted(String effectKey, EntityType<?> entityType) {
        return EFFECT_BLACKLISTS.getOrDefault(effectKey, Collections.emptySet()).contains(entityType);
    }

    public static void loadExternalBlacklist() {
        Path configPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "/blacklist.json");
        if (Files.exists(configPath)) {
            try {
                load(Files.newBufferedReader(configPath));
            } catch (IOException e) {
                DontGetHurt.LOGGER.error("Failed to load external effect blacklist", e);
            }
        }
    }

    private static void load(Reader json) {
        JsonObject root = GSON.fromJson(json, JsonObject.class);
        parseBlacklist(root, PULSE_EFFECT);
    }
}
