package com.lastimp.dgh.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lastimp.dgh.DontGetHurt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HealthLivingEntityList {
    public static final String WHITE_LIST_ID = "health_white_list_1.2.10";
    public static final String PLAYER_BLACK_LIST_ID = "player_black_list";
    public static final int ENV_RESIST = 0;
    public static final int ENTITY_RESIST = 1;
    public static final int PLAYER_RESIST = 2;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<EntityType<?>, WhiteListRecord> HEALTH_WHITE_LIST = new HashMap();
    private static final Set<UUID> HEALTH_PLAYER_BLACK_LIST = new HashSet<>();

    public static boolean isEntityWhitelisted(EntityType<?> entityType) {
        return HEALTH_WHITE_LIST.containsKey(entityType);
    }

    public static boolean isPlayerBlacklisted(Player player) {
        return HEALTH_PLAYER_BLACK_LIST.contains(player.getUUID());
    }

    public static float getEntityDownResist(EntityType<?> entityType, int resist_type) {
        if (!isEntityWhitelisted(entityType)) return 1.0f;
        if (resist_type == ENV_RESIST) {
            return HEALTH_WHITE_LIST.get(entityType).DOWN_DAMAGE_RESISTANCE_ENV();
        } else if (resist_type == ENTITY_RESIST) {
            return HEALTH_WHITE_LIST.get(entityType).DOWN_DAMAGE_RESISTANCE_ENTITY();
        } else if (resist_type == PLAYER_RESIST) {
            return HEALTH_WHITE_LIST.get(entityType).DOWN_DAMAGE_RESISTANCE_PLAYER();
        }
        return 1.0f;
    }

    public static boolean canEntityLieDown(EntityType<?> entityType) {
        if (!isEntityWhitelisted(entityType)) return false;
        return HEALTH_WHITE_LIST.get(entityType).CAN_LIE_DOWN();
    }

    public static Set<EntityType<?>> getWhiteList() {
        return new HashSet<>(HEALTH_WHITE_LIST.keySet());
    }

    public static void loadExternallist() {
        Path whiteListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + WHITE_LIST_ID + ".json");
        Path blackListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + PLAYER_BLACK_LIST_ID + ".json");

        try {
            if (!Files.exists(whiteListConfigPath)) {
                var writer = Files.newBufferedWriter(Files.createFile(whiteListConfigPath));
                writer.write("[\n" +
                        "  {\n" +
                        "    \"id\": \"minecraft:player\",\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENV\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\": 0.1,\n" +
                        "    \"CAN_LIE_DOWN\": true\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\": \"minecraft:villager\",\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENV\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\": 0.1,\n" +
                        "    \"CAN_LIE_DOWN\": true\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"id\": \"touhou_little_maid:maid\",\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENV\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\": 0.1,\n" +
                        "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\": 0.1,\n" +
                        "    \"CAN_LIE_DOWN\": true\n" +
                        "  }\n" +
                        "]");
                writer.close();
            }
            loadWhiteList(Files.newBufferedReader(whiteListConfigPath));
            if (!Files.exists(blackListConfigPath)) {
                var writer = Files.newBufferedWriter(Files.createFile(blackListConfigPath));
                writer.write("[]");
                writer.close();
            }
            loadPlayerBlackList(Files.newBufferedReader(blackListConfigPath));
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load external health whitelist", e);
        }
    }

    public static void loadServerData(String json) {
        JsonArray root = HealthLivingEntityList.GSON.fromJson(json, JsonArray.class);
        root.forEach(jsonElement -> EntityType.byString(jsonElement.getAsString()).ifPresent(type -> HEALTH_WHITE_LIST.put(type, WhiteListRecord.DEFAULT)));
    }

    private static void loadWhiteList(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        root.forEach(jsonElement -> {
            JsonObject element = (JsonObject) jsonElement;

            float evn = element.has("DOWN_DAMAGE_RESISTANCE_ENV") ? element.get("DOWN_DAMAGE_RESISTANCE_ENV").getAsFloat() : 0.1f;
            float entity = element.has("DOWN_DAMAGE_RESISTANCE_ENTITY") ? element.get("DOWN_DAMAGE_RESISTANCE_ENTITY").getAsFloat() : 0.1f;
            float player = element.has("DOWN_DAMAGE_RESISTANCE_PLAYER") ? element.get("DOWN_DAMAGE_RESISTANCE_PLAYER").getAsFloat() : 0.1f;
            boolean canLieDown = !element.has("CAN_LIE_DOWN") || element.get("CAN_LIE_DOWN").getAsBoolean();
            EntityType.byString(element.get("id").getAsString()).ifPresent(type -> HEALTH_WHITE_LIST.put(type, new WhiteListRecord(evn, entity, player, canLieDown)));
        });
    }

    private static void loadPlayerBlackList(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        parsePlayerBlacklist(root);
    }

    private static void parsePlayerBlacklist(JsonArray root) {
        root.forEach(jsonElement -> {
            String playerUUID = jsonElement.getAsString();
            HEALTH_PLAYER_BLACK_LIST.add(UUID.fromString(playerUUID));
        });
    }
}
