package com.lastimp.dgh.common.config;

import net.minecraft.nbt.CompoundTag;

public record WhiteListRecord(
        float DOWN_DAMAGE_RESISTANCE_ENV,
        float DOWN_DAMAGE_RESISTANCE_ENTITY,
        float DOWN_DAMAGE_RESISTANCE_PLAYER,
        boolean CAN_LIE_DOWN,
        boolean CAN_BE_SEEN_WHEN_LYING
) {
    public static CompoundTag serializeNBT(WhiteListRecord record) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("down_damage_resist_env", record.DOWN_DAMAGE_RESISTANCE_ENV);
        tag.putFloat("down_damage_resist_entity", record.DOWN_DAMAGE_RESISTANCE_ENTITY);
        tag.putFloat("down_damage_resist_player", record.DOWN_DAMAGE_RESISTANCE_PLAYER);
        tag.putBoolean("can_lie_down", record.CAN_LIE_DOWN);
        tag.putBoolean("can_be_seen_when_lying", record.CAN_BE_SEEN_WHEN_LYING);
        return tag;
    }

    public static WhiteListRecord deserializeNBT(CompoundTag nbt) {
        var DOWN_DAMAGE_RESISTANCE_ENV = nbt.getFloat("down_damage_resist_env");
        var DOWN_DAMAGE_RESISTANCE_ENTITY = nbt.getFloat("down_damage_resist_entity");
        var DOWN_DAMAGE_RESISTANCE_PLAYER = nbt.getFloat("down_damage_resist_player");
        var CAN_LIE_DOWN = nbt.getBoolean("can_lie_down");
        var CAN_BE_SEEN_WHEN_LYING = nbt.getBoolean("can_be_seen_when_lying");
        return new WhiteListRecord(DOWN_DAMAGE_RESISTANCE_ENV, DOWN_DAMAGE_RESISTANCE_ENTITY, DOWN_DAMAGE_RESISTANCE_PLAYER, CAN_LIE_DOWN, CAN_BE_SEEN_WHEN_LYING);
    }
}
