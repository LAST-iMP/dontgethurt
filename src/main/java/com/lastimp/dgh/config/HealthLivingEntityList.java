package com.lastimp.dgh.config;

import com.google.gson.*;
import com.lastimp.dgh.DontGetHurt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class HealthLivingEntityList {
    public static final String EXAMPLE =
            "[\n" +
            "  {\n" +
            "    \"id\":\"minecraft:player\",\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
            "    \"CAN_LIE_DOWN\":true,\n" +
            "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\":\"minecraft:villager\",\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
            "    \"CAN_LIE_DOWN\":true,\n" +
            "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\":\"touhou_little_maid:maid\",\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
            "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
            "    \"CAN_LIE_DOWN\":true,\n" +
            "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
            "  }\n" +
            "]";
    public static final String WHITE_LIST_ID = "health_white_list_1.3.0";
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

    public static boolean canBeSeenWhenLying(EntityType<?> entityType) {
        if (!isEntityWhitelisted(entityType)) return false;
        return HEALTH_WHITE_LIST.get(entityType).CAN_BE_SEEN_WHEN_LYING();
    }

    public static CompoundTag getConfig() {
        CompoundTag tag = new CompoundTag();

        ListTag blackList = new ListTag();
        HEALTH_PLAYER_BLACK_LIST.stream().forEach(uuid -> blackList.add(StringTag.valueOf(uuid.toString())));
        tag.put("player_black_list", blackList);

        ListTag whiteList = new ListTag();
        for (var key : HEALTH_WHITE_LIST.keySet()) {
            CompoundTag entity = new CompoundTag();
            entity.putString("id", key.toString());
            entity.put("value", WhiteListRecord.serializeNBT(HEALTH_WHITE_LIST.get(key)));
            whiteList.add(entity);
        }
        tag.put("player_white_list", whiteList);
        return tag;
    }

    public static void loadServerData(CompoundTag tag) {
        ListTag blackList = tag.getList("player_black_list", ListTag.TAG_STRING);
        blackList.forEach(element -> HEALTH_PLAYER_BLACK_LIST.add(UUID.fromString(element.getAsString())));
        ListTag whiteList = tag.getList("player_black_list", ListTag.TAG_COMPOUND);
        whiteList.forEach(element -> {
            CompoundTag entity = (CompoundTag) element;
            EntityType.byString(entity.getString("id")).ifPresent(type ->
                    HEALTH_WHITE_LIST.put(type, WhiteListRecord.deserializeNBT(entity.getCompound("value")))
            );
        });
    }

    public static void loadExternallist() {
        Path whiteListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + WHITE_LIST_ID + ".json");
        Path blackListConfigPath = FMLPaths.CONFIGDIR.get().resolve(DontGetHurt.MODID + "-" + PLAYER_BLACK_LIST_ID + ".json");

        try {
            if (!Files.exists(whiteListConfigPath)) {
                write(Files.createFile(whiteListConfigPath), EXAMPLE);
            }
            loadWhiteList(Files.newBufferedReader(whiteListConfigPath));
            if (!Files.exists(blackListConfigPath)) {
                write(Files.createFile(blackListConfigPath), "[]");
            }
            loadPlayerBlackList(Files.newBufferedReader(blackListConfigPath));
        } catch (IOException e) {
            DontGetHurt.LOGGER.error("Failed to load external health whitelist", e);
        }
    }

    public static void write(Path path, String context) throws IOException{
        var writer = Files.newBufferedWriter(path);
        writer.write(context);
        writer.close();
    }

    private static void loadWhiteList(Reader json) {
        JsonArray root = GSON.fromJson(json, JsonArray.class);
        root.forEach(jsonElement -> {
            JsonObject element = (JsonObject) jsonElement;

            float evn = readOrWriteDefault(element, "DOWN_DAMAGE_RESISTANCE_ENV", 0.1f);
            float entity = readOrWriteDefault(element, "DOWN_DAMAGE_RESISTANCE_ENTITY", 0.1f);
            float player = readOrWriteDefault(element, "DOWN_DAMAGE_RESISTANCE_PLAYER", 0.1f);
            boolean canLieDown = readOrWriteDefault(element, "CAN_LIE_DOWN", true) && Config.allow_down;
            boolean canBeSeen = readOrWriteDefault(element, "CAN_BE_SEEN_WHEN_LYING", false);
            EntityType.byString(element.get("id").getAsString()).ifPresent(type -> HEALTH_WHITE_LIST.put(type, new WhiteListRecord(evn, entity, player, canLieDown, canBeSeen)));
        });
    }

    private static float readOrWriteDefault(JsonObject element, String key, float defaultValue) {
        if (!element.has(key)) {
            element.addProperty(key, defaultValue);
            return defaultValue;
        } else {
            return element.get(key).getAsFloat();
        }
    }

    private static boolean readOrWriteDefault(JsonObject element, String key, boolean defaultValue) {
        if (!element.has(key)) {
            element.addProperty(key, defaultValue);
            return defaultValue;
        } else {
            return element.get(key).getAsBoolean();
        }
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
