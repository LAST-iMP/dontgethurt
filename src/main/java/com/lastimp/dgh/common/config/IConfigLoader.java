package com.lastimp.dgh.common.config;

import com.google.gson.*;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public interface IConfigLoader<T> {
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    String getID();

    int listType();

    Set<T> get();

    Tag save(T data);

    void read(Tag tag);

    void load(JsonElement element);

    String example();

    default CompoundTag getConfig() {
        CompoundTag tag = new CompoundTag();
        ListTag list = IConfigLoader.getListTag(this.get(),this::save);
        tag.put(this.getID(), list);
        return tag;
    }

    default void loadServerData(CompoundTag tag) {
        ListTag list = tag.getList(this.getID(), this.listType());
        list.forEach(this::read);
    }

    default void loadExternalList() {
        IConfigLoader.loadExternalList(this.getID(), this::load, this.example());
    }

    static <T> ListTag getListTag(Collection<T> data, Function<T, Tag> func) {
        ListTag list = new ListTag();
        data.forEach(element -> list.add(func.apply(element)));
        return list;
    }

    static void write(Path path, String context) throws IOException{
        try (var writer = Files.newBufferedWriter(path, UTF_8, CREATE_NEW, WRITE)) {
            writer.write(context);
        }
    }

    static void loadExternalList(String id, Consumer<JsonElement> consumer, String example) {
        Path path = null;

        try {
            path = PlatformService.CONFIG.getConfigRoot()
                    .resolve(Path.of("dgh", Utils.MODID + "-" + id + ".json"));

            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Utils.LOGGER.info("Creating dgh config file: {}", path);
                write(path, example);
                Utils.LOGGER.info("Created dgh config file: {}", path);
            }

            try (var reader = Files.newBufferedReader(path, UTF_8)) {
                loadJson(reader, consumer);
                Utils.LOGGER.info("Loaded dgh config file: {}", path);
            }
        } catch (Exception e) {
            Utils.LOGGER.error("Failed to load dgh external config: id={}, path={}", id, path, e);
        }
    }

    static float readOrWriteDefault(JsonObject element, String key, float defaultValue) {
        if (!element.has(key)) {
            element.addProperty(key, defaultValue);
            return defaultValue;
        } else {
            return element.get(key).getAsFloat();
        }
    }

    static boolean readOrWriteDefault(JsonObject element, String key, boolean defaultValue) {
        if (!element.has(key)) {
            element.addProperty(key, defaultValue);
            return defaultValue;
        } else {
            return element.get(key).getAsBoolean();
        }
    }

    static void loadJson(Reader json, Consumer<JsonElement> consumer) {
        JsonArray root = IConfigLoader.GSON.fromJson(json, JsonArray.class);
        root.forEach(consumer);
    }
}
