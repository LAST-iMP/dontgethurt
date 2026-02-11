package com.lastimp.dgh.common.config;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.config.impl.HealthLivingEntityList;
import com.lastimp.dgh.common.config.impl.PlayerBlackList;
import com.lastimp.dgh.common.network.message.MyServerConfigSynData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public abstract class ModConfigs {
    private static final HealthLivingEntityList healthList = new HealthLivingEntityList();
    private static final PlayerBlackList playerBlackList = new PlayerBlackList();

    public static CompoundTag getCompound(Type type) {
        switch (type) {
            case HEALTH -> {
                return healthList.getConfig();
            }
            case BLACKLIST -> {
                return playerBlackList.getConfig();
            }
            default -> {
                return new CompoundTag();
            }
        }
    }

    public static void loadExternalList() {
        healthList.loadExternalList();
        playerBlackList.loadExternalList();
    }

    public static void loadServerData(Type type, CompoundTag data) {
        switch (type) {
            case HEALTH -> healthList.loadServerData(data);
            case BLACKLIST -> playerBlackList.loadServerData(data);
        }
    }

    public static void synToPlayer(ServerPlayer player) {
        PlatformService.NETWORK.sendToPlayer(player, MyServerConfigSynData.getInstance(ModConfigs.Type.HEALTH));
        PlatformService.NETWORK.sendToPlayer(player, MyServerConfigSynData.getInstance(ModConfigs.Type.BLACKLIST));
    }

    public enum Type {
        HEALTH, BLACKLIST
    }
}
