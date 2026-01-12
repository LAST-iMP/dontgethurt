package com.lastimp.dgh.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.lastimp.dgh.DontGetHurt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HealthLivingEntityList {
    public static final String WHITE_LIST_ID = "health_white_list";
    public static final String PLAYER_BLACK_LIST_ID = "player_black_list";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Set<EntityType<?>> HEALTH_WHITE_LIST = new HashSet<>();
    private static final Set<UUID> HEALTH_PLAYER_BLACK_LIST = new HashSet<>();

    public static boolean isEntityWhitelisted(EntityType<?> entityType) {
        return HEALTH_WHITE_LIST.contains(entityType);
    }

    public static boolean isPlayerBlacklisted(Player player) {
        return HEALTH_PLAYER_BLACK_LIST.contains(player.getUUID());
    }

    public static Set<EntityType<?>> getWhiteList() {
        return new HashSet<>(HEALTH_WHITE_LIST);
    }

    public static Set<UUID> getPlayerBlackList() {
        return new HashSet<>(HEALTH_PLAYER_BLACK_LIST);
    }

    public static void addWhiteList(EntityType<?> entityType) {
        HEALTH_WHITE_LIST.add(entityType);
    }

    public static void loadWhiteLists() {
        HEALTH_WHITE_LIST.add(EntityType.PLAYER);
        HEALTH_WHITE_LIST.add(EntityType.VILLAGER);
    }

    public static void loadExternallist() {
        Path whiteListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + WHITE_LIST_ID + ".json");
        Path blackListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + PLAYER_BLACK_LIST_ID + ".json");

        try {
            if (Files.exists(whiteListConfigPath)) {
                loadWhiteList(Files.newBufferedReader(whiteListConfigPath));
            } else {
                var writer = Files.newBufferedWriter(Files.createFile(whiteListConfigPath));
                writer.write("[]");
                writer.close();
            }
            if (Files.exists(blackListConfigPath)) {
                loadPlayerBlackList(Files.newBufferedReader(blackListConfigPath));
            } else {
                var writer = Files.newBufferedWriter(Files.createFile(blackListConfigPath));
                writer.write("[]");
                writer.close();
            }
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load external health whitelist", e);
        }
    }

    public static void loadServerData(String json) {
        JsonArray root = HealthLivingEntityList.GSON.fromJson(json, JsonArray.class);
        HealthLivingEntityList.parseHealthWhitelist(root);
    }

    private static void loadWhiteList(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        parseHealthWhitelist(root);
    }

    private static void loadPlayerBlackList(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        parsePlayerBlacklist(root);
    }

    private static void parseHealthWhitelist(JsonArray root) {
        root.forEach(jsonElement -> {
            String entityId = jsonElement.getAsString();
            EntityType.byString(entityId).ifPresent(HEALTH_WHITE_LIST::add);
        });
    }

    private static void parsePlayerBlacklist(JsonArray root) {
        root.forEach(jsonElement -> {
            String playerUUID = jsonElement.getAsString();
            HEALTH_PLAYER_BLACK_LIST.add(UUID.fromString(playerUUID));
        });
    }
}
