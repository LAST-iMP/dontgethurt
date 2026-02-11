package com.lastimp.dgh.common.config.record;

import net.minecraft.nbt.CompoundTag;

public record ArmorListRecord(
        float burn_resist,
        float open_resist,
        float internal_resist,
        float burn_tough,
        float open_tough,
        float internal_tough
) {
    public static CompoundTag serialize(ArmorListRecord data) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("burn_resist", data.burn_resist);
        tag.putFloat("open_resist", data.open_resist);
        tag.putFloat("internal_resist", data.internal_resist);
        tag.putFloat("burn_tough", data.burn_tough);
        tag.putFloat("open_tough", data.open_tough);
        tag.putFloat("internal_tough", data.internal_tough);
        return tag;
    }

    public static ArmorListRecord deserialize(CompoundTag tag) {
        var burn_resist = tag.getFloat("burn_resist");
        var open_resist = tag.getFloat("open_resist");
        var internal_resist = tag.getFloat("internal_resist");
        var burn_tough = tag.getFloat("burn_tough");
        var open_tough = tag.getFloat("open_tough");
        var internal_tough = tag.getFloat("internal_tough");
        return new ArmorListRecord(burn_resist, open_resist, internal_resist, burn_tough, open_tough, internal_tough);
    }
}
