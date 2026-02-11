package com.lastimp.dgh.common.config.impl;

import com.google.gson.*;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.config.IConfigLoader;
import com.lastimp.dgh.common.config.record.ArmorListRecord;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ArmorList implements IConfigLoader<Map.Entry<ResourceLocation, ArmorListRecord>> {
    private static final Map<ResourceLocation, ArmorListRecord> ARMOR_LIST = new HashMap<>();

    public static boolean has(Item item) {
        var id = PlatformService.REGISTRY_HANDLER.itemID(item);
        return ARMOR_LIST.containsKey(id);
    }

    public static ArmorListRecord getData(Item item) {
        var id = PlatformService.REGISTRY_HANDLER.itemID(item);
        return ARMOR_LIST.get(id);
    }

    @Override
    public String getID() {
        return "armor_config_list";
    }

    @Override
    public int listType() {
        return ListTag.TAG_COMPOUND;
    }

    @Override
    public Set<Map.Entry<ResourceLocation, ArmorListRecord>> get() {
        return ARMOR_LIST.entrySet();
    }

    @Override
    public Tag save(Map.Entry<ResourceLocation, ArmorListRecord> data) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", data.getKey().toString());
        tag.put("value", ArmorListRecord.serialize(data.getValue()));
        return tag;
    }

    @Override
    public void read(Tag tag) {
        CompoundTag armorData = (CompoundTag) tag;
        var id = ResourceLocation.parse(armorData.getString("id"));
        this.put(id, ArmorListRecord.deserialize(armorData.getCompound("value")));
    }

    @Override
    public void load(JsonElement element) {
        JsonObject json = (JsonObject) element;

        var id = ResourceLocation.parse(json.get("id").getAsString());
        float burn_resist = IConfigLoader.readOrWriteDefault(json, "BURN_RESIST", 0);
        float open_resist = IConfigLoader.readOrWriteDefault(json, "OPEN_RESIST", 0);
        float internal_resist = IConfigLoader.readOrWriteDefault(json, "INTERNAL_RESIST", 0);
        float burn_tough = IConfigLoader.readOrWriteDefault(json, "BURN_TOUGH", 0);
        float open_tough = IConfigLoader.readOrWriteDefault(json, "OPEN_TOUGH", 0);
        float internal_tough = IConfigLoader.readOrWriteDefault(json, "INTERNAL_TOUGH", 0);
        this.put(id, new ArmorListRecord(burn_resist, open_resist, internal_resist, burn_tough, open_tough, internal_tough));
    }

    private void put(ResourceLocation id, ArmorListRecord record) {
        ARMOR_LIST.put(id, record);
    }

    @Override
    public String example() {
        return "[\n" +
                "  {\n" +
                "    \"id\":\"minecraft:turtle_helmet\",\n" +
                "    \"BURN_RESIST\":0,\n" +
                "    \"OPEN_RESIST\":20,\n" +
                "    \"INTERNAL_RESIST\":0,\n" +
                "    \"BURN_TOUGH\":0,\n" +
                "    \"OPEN_TOUGH\":0,\n" +
                "    \"INTERNAL_TOUGH\":0\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:leather_helmet\",\n" +
                "    \"OPEN_RESIST\":10,\n" +
                "    \"INTERNAL_RESIST\":10\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:leather_chestplate\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"INTERNAL_RESIST\":10\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:leather_leggings\",\n" +
                "    \"OPEN_RESIST\":20,\n" +
                "    \"INTERNAL_RESIST\":10\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:leather_boots\",\n" +
                "    \"OPEN_RESIST\":10,\n" +
                "    \"INTERNAL_RESIST\":10\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:golden_helmet\",\n" +
                "    \"OPEN_RESIST\":20,\n" +
                "    \"INTERNAL_RESIST\":40\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:golden_chestplate\",\n" +
                "    \"OPEN_RESIST\":50,\n" +
                "    \"INTERNAL_RESIST\":100\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:golden_leggings\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"INTERNAL_RESIST\":60\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:golden_boots\",\n" +
                "    \"OPEN_RESIST\":10,\n" +
                "    \"INTERNAL_RESIST\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:chainmail_helmet\",\n" +
                "    \"OPEN_RESIST\":20,\n" +
                "    \"INTERNAL_RESIST\":40\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:chainmail_chestplate\",\n" +
                "    \"OPEN_RESIST\":50,\n" +
                "    \"INTERNAL_RESIST\":100\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:chainmail_leggings\",\n" +
                "    \"OPEN_RESIST\":40,\n" +
                "    \"INTERNAL_RESIST\":80\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:chainmail_boots\",\n" +
                "    \"OPEN_RESIST\":10,\n" +
                "    \"INTERNAL_RESIST\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:iron_helmet\",\n" +
                "    \"OPEN_RESIST\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:iron_chestplate\",\n" +
                "    \"OPEN_RESIST\":60\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:iron_leggings\",\n" +
                "    \"OPEN_RESIST\":50\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:iron_boots\",\n" +
                "    \"OPEN_RESIST\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:diamond_helmet\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"OPEN_TOUGH\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:diamond_chestplate\",\n" +
                "    \"OPEN_RESIST\":80,\n" +
                "    \"OPEN_TOUGH\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:diamond_leggings\",\n" +
                "    \"OPEN_RESIST\":60,\n" +
                "    \"OPEN_TOUGH\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:diamond_boots\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"OPEN_TOUGH\":20\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:netherite_helmet\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"OPEN_TOUGH\":30\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:netherite_chestplate\",\n" +
                "    \"OPEN_RESIST\":80,\n" +
                "    \"OPEN_TOUGH\":30\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:netherite_leggings\",\n" +
                "    \"OPEN_RESIST\":60,\n" +
                "    \"OPEN_TOUGH\":30\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"minecraft:netherite_boots\",\n" +
                "    \"OPEN_RESIST\":30,\n" +
                "    \"OPEN_TOUGH\":30\n" +
                "  }\n" +
                "]";
    }
}
