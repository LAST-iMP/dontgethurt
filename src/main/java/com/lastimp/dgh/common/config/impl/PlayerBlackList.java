package com.lastimp.dgh.common.config.impl;

import com.google.gson.JsonElement;
import com.lastimp.dgh.common.config.IConfigLoader;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerBlackList implements IConfigLoader<UUID> {
    private static final Set<UUID> HEALTH_PLAYER_BLACK_LIST = new HashSet<>();

    public static boolean isPlayerBlacklisted(Player player) {
        return HEALTH_PLAYER_BLACK_LIST.contains(player.getUUID());
    }

    @Override
    public String getID() {
        return "player_black_list";
    }

    @Override
    public int listType() {
        return ListTag.TAG_STRING;
    }

    @Override
    public Set<UUID> get() {
        return HEALTH_PLAYER_BLACK_LIST;
    }

    @Override
    public Tag save(UUID data) {
        return StringTag.valueOf(data.toString());
    }

    @Override
    public void read(Tag tag) {
        HEALTH_PLAYER_BLACK_LIST.add(UUID.fromString(tag.getAsString()));
    }

    @Override
    public void load(JsonElement element) {
        String playerUUID = element.getAsString();
        HEALTH_PLAYER_BLACK_LIST.add(UUID.fromString(playerUUID));
    }

    @Override
    public String example() {
        return "[]";
    }
}
