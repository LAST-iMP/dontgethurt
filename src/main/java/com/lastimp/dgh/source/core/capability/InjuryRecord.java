package com.lastimp.dgh.source.core.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class InjuryRecord implements INBTSerializable<CompoundTag> {
    private static final Component CAUSE = Component.literal("导致\n");
    private static final Component WITH = Component.literal("并导致\n");
    private String source;
    private String body;
    private String condition;
    private float value;
    private int level = 0;

    protected InjuryRecord() {
        this(null, null, null, 0);
    }

    public InjuryRecord(String source, String body, String condition, float value) {
        this(source, body, condition, value, 0);
    }

    public InjuryRecord(String source, String body, String condition, float value, int level) {
        this.source = source;
        this.body = body;
        this.condition = condition;
        this.value = value;
        this.level = level;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putString("source", source);
        tag.putString("body", body);
        tag.putString("condition", condition);
        tag.putFloat("value", value);
        tag.putInt("level", level);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, CompoundTag nbt) {
        this.source = nbt.getString("source");
        this.body = nbt.getString("body");
        this.condition = nbt.getString("condition");
        this.value = nbt.getFloat("value");
        this.level = nbt.getInt("level");
    }

    public Component getComponent(){
        Component value = this.value > 0 ? Component.literal(String.format("%.2f", Math.max(this.value, 0.01f))) : Component.empty();
        if (this.level == 0) {
            return Component
                    .translatable(this.source)
                    .append(CAUSE)
                    .append(Component.literal("*  "))
                    .append(Component.translatable(this.body)) //2
                    .append(Component.translatable(this.condition)) //2-5
                    .append(value);//4
        } else {
            Component indent = Component.literal(" ".repeat(2 * this.level));
            return Component
                    .literal("#").append(indent)
                    .append(WITH)
                    .append(Component.literal("*  ")).append(indent)
                    .append(Component.translatable(this.body)) //2
                    .append(Component.translatable(this.condition)) //2-5
                    .append(value);//4
        }
    }

    @Override
    public String toString() {
        return getComponent().getString();
    }

    public static InjuryRecord phrase(HolderLookup.Provider provider, CompoundTag nbt) {
        InjuryRecord record = new InjuryRecord();
        record.deserializeNBT(provider, nbt);
        return record;
    }
}
