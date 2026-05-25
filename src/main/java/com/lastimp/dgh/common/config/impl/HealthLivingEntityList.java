package com.lastimp.dgh.common.config.impl;

import com.google.gson.*;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.config.IConfigLoader;
import com.lastimp.dgh.common.config.record.WhiteListRecord;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;

import java.util.*;

public class HealthLivingEntityList implements IConfigLoader<Map.Entry<EntityType<?>, WhiteListRecord>>{
    public static final int ENV_RESIST = 0;
    public static final int ENTITY_RESIST = 1;
    public static final int PLAYER_RESIST = 2;

    private static final Map<EntityType<?>, WhiteListRecord> HEALTH_WHITE_LIST = new HashMap<>();

    public static boolean isEntityWhitelisted(EntityType<?> entityType) {
        return HEALTH_WHITE_LIST.containsKey(entityType);
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

    public static boolean callWhenDying(EntityType<?> entityType) {
        if (!isEntityWhitelisted(entityType)) return false;
        return HEALTH_WHITE_LIST.get(entityType).CALL_WHEN_DYING();
    }

    @Override
    public String getID() {
        return "health_white_list_1.3.0";
    }

    @Override
    public int listType() {
        return ListTag.TAG_COMPOUND;
    }

    @Override
    public Set<Map.Entry<EntityType<?>, WhiteListRecord>> get() {
        return HEALTH_WHITE_LIST.entrySet();
    }

    @Override
    public Tag save(Map.Entry<EntityType<?>, WhiteListRecord> data) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", data.getKey().toString());
        tag.put("value", WhiteListRecord.serializeNBT(data.getValue()));
        return tag;
    }

    @Override
    public void read(Tag tag) {
        CompoundTag entity = (CompoundTag) tag;
        EntityType.byString(entity.getString("id")).ifPresent(type ->
                HEALTH_WHITE_LIST.put(type, WhiteListRecord.deserializeNBT(entity.getCompound("value")))
        );
    }

    @Override
    public void load(JsonElement element) {
        JsonObject json = (JsonObject) element;
        float evn = IConfigLoader.readOrWriteDefault(json, "DOWN_DAMAGE_RESISTANCE_ENV", 0.1f);
        float entity = IConfigLoader.readOrWriteDefault(json, "DOWN_DAMAGE_RESISTANCE_ENTITY", 0.1f);
        float player = IConfigLoader.readOrWriteDefault(json, "DOWN_DAMAGE_RESISTANCE_PLAYER", 0.1f);
        boolean canLieDown = IConfigLoader.readOrWriteDefault(json, "CAN_LIE_DOWN", true) && PlatformService.CONFIG.ALLOW_DOWN();
        boolean canBeSeen = IConfigLoader.readOrWriteDefault(json, "CAN_BE_SEEN_WHEN_LYING", false);
        boolean callWhenDying = IConfigLoader.readOrWriteDefault(json, "CALL_WHEN_DYING", true);
        EntityType.byString(json.get("id").getAsString()).ifPresent(type -> HEALTH_WHITE_LIST.put(type, new WhiteListRecord(evn, entity, player, canLieDown, canBeSeen, callWhenDying)));
    }

    @Override
    public String example() {
        return "[\n" +
                "  {\n" +
                "    \"id\":\"minecraft:player\",\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
                "    \"CAN_LIE_DOWN\":true,\n" +
                "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
                "    \"CALL_WHEN_DYING\":true\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:villager\",\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
                "    \"CAN_LIE_DOWN\":true,\n" +
                "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
                "    \"CALL_WHEN_DYING\":false\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"touhou_little_maid:maid\",\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENV\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_ENTITY\":0.1,\n" +
                "    \"DOWN_DAMAGE_RESISTANCE_PLAYER\":0.1,\n" +
                "    \"CAN_LIE_DOWN\":true,\n" +
                "    \"CAN_BE_SEEN_WHEN_LYING\":false\n" +
                "    \"CALL_WHEN_DYING\":true\n" +
                "  }\n" +
                "]";
    }
}
