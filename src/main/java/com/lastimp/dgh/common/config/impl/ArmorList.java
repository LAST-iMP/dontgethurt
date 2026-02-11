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
        float burn_resist = IConfigLoader.readOrWriteDefault(json, "burn_resist", 0);
        float open_resist = IConfigLoader.readOrWriteDefault(json, "open_resist", 0);
        float internal_resist = IConfigLoader.readOrWriteDefault(json, "internal_resist", 0);
        float burn_tough = IConfigLoader.readOrWriteDefault(json, "burn_tough", 0);
        float open_tough = IConfigLoader.readOrWriteDefault(json, "open_tough", 0);
        float internal_tough = IConfigLoader.readOrWriteDefault(json, "internal_tough", 0);
        this.put(id, new ArmorListRecord(burn_resist, open_resist, internal_resist, burn_tough, open_tough, internal_tough));
    }

    @Override
    public String example() {
        return "";
    }

    private void put(ResourceLocation id, ArmorListRecord record) {
        ARMOR_LIST.put(id, record);
    }
}
