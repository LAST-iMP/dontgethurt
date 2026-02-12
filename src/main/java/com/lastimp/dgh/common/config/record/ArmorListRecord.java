package com.lastimp.dgh.common.config.record;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.config.impl.ArmorList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record ArmorListRecord (
        float burn_resist,
        float open_resist,
        float internal_resist,
        float burn_tough,
        float open_tough,
        float internal_tough
) {
    public float locToRes(ResourceLocation location) {
        return this.locToFloat(location, false);
    }

    public float locToTough(ResourceLocation location) {
        return this.locToFloat(location, true);
    }

    public float locToFloat(ResourceLocation location, boolean isTough) {
        if (location.equals(BodyCondition.BURN_RES)) {
            return isTough ? this.burn_tough() : this.burn_resist();
        } else if (location.equals(BodyCondition.INTERNAL_RES)) {
            return isTough ? this.internal_tough() : this.internal_resist();
        } else if (location.equals(BodyCondition.OPEN_WOUND_RES)) {
            return isTough ? this.open_tough() : this.open_resist();
        }
        return 0;
    }

    public static ArmorListRecord combine(LivingEntity livingEntity, EquipmentSlot...slots) {
        ItemStack[] stacks = new ItemStack[slots.length];
        for (int i = 0; i < slots.length; i++) {
            stacks[i] = livingEntity.getItemBySlot(slots[i]);
        }
        return combine(stacks);
    }

    public static ArmorListRecord combine(ItemStack ...stacks) {
        ArmorListRecord[] records = new ArmorListRecord[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            var item = stacks[i].getItem();
            records[i] = ArmorList.has(item) ? ArmorList.getData(item) : null;
        }
        return combine(records);
    }

    public static ArmorListRecord combine(ArmorListRecord ...records) {
        float burn_resist = 0, open_resist = 0, internal_resist = 0, burn_tough = 0, open_tough = 0, internal_tough = 0;
        for (var record : records) {
            if (record == null) continue;
            burn_resist += record.burn_resist;
            open_resist += record.open_resist;
            internal_resist += record.internal_resist;
            burn_tough += record.burn_tough;
            open_tough += record.open_tough;
            internal_tough += record.internal_tough;
        }
        return new ArmorListRecord(burn_resist, open_resist, internal_resist, burn_tough, open_tough, internal_tough);
    }

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
