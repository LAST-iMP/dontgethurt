
package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class AbstractBody implements INBTSerializable<CompoundTag> {

    private final HashMap<BodyCondition, ConditionState> state = new HashMap<>();

    public AbstractBody() {
        for (BodyCondition condition : this.getBodyConditions()) {
            state.put(condition, new ConditionState(condition.defaultValue));
        }
    }

    public abstract List<BodyCondition> getBodyConditions();

    public abstract float getVitalityWeight();

    public float getConditionValue(BodyCondition key) {
        return this.getCondition(key).getValue();
    }

    public ConditionState getCondition(BodyCondition key) {
        return state.get(key);
    }

    public void setConditionValue(BodyCondition key, float value) {
        ConditionState state = this.state.get(key);
        state.setValue(Mth.clamp(value, key.minValue, key.maxValue));
    }

    public void addConditionValue(BodyCondition key, float value) {
        float newValue = this.getConditionValue(key) + value;
        this.setConditionValue(key, newValue);
    }

    public void injury(BodyCondition key, float value) {
        this.addConditionValue(key, value);
    }

    public void injuryHidden(BodyCondition key, float value) {
        ConditionState state = this.state.get(key);
        state.setHiddenValue(Mth.clamp(state.getHiddenValue() + value, key.minValue, key.maxValue));
    }

    public void healing(BodyCondition key, float value) {
        this.addConditionValue(key, value);
    }

    public void healingHidden(BodyCondition key, float value) {
        ConditionState state = this.state.get(key);
        state.setHiddenValue(Mth.clamp(state.getHiddenValue() + value, key.minValue, key.maxValue));
    }

    public abstract AbstractBody update(PlayerHealthCapability health, Player player);

    public AbstractBody updatePre(PlayerHealthCapability health, Player player) {
        return this;
    }

    public AbstractBody updatePost(PlayerHealthCapability health, Player player) {
        this.updateDisplayValue(health);
        return this;
    }

    public abstract float updateVitalityLost(PlayerHealthCapability health, Player player);

    public abstract int slowDownLevel(PlayerHealthCapability health);

    public void updateDisplayValue(PlayerHealthCapability health) {
        for (BodyCondition condition : this.getBodyConditions()) {
            ConditionState state = this.getCondition(condition);
            state.tick();
        }
    }

    public boolean abnormalWithHidden(BodyCondition condition) {
        ConditionState state = this.getCondition(condition);
        return condition.abnormal(state.getValue()) || condition.abnormal(state.getHiddenValue());
    }

    public boolean abnormal(BodyCondition condition) {
        return condition.abnormal(this.getConditionValue(condition));
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<BodyCondition, ConditionState> e : state.entrySet()) {
            tag.put(e.getKey().name(), e.getValue().serializeNBT(provider));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        for (BodyCondition condition : this.getBodyConditions()) {
            if (nbt.contains(condition.name())) {
                state.get(condition).deserializeNBT(provider, nbt.getCompound(condition.name()));
            } else {
                state.put(condition, new ConditionState(condition.defaultValue));
            }
        }
    }

    public static <T extends AbstractBody> AbstractBody buildFromNBT(HolderLookup.Provider provider, CompoundTag nbt, Function<Void, T> constructor) {
        T body = constructor.apply(null);
        body.deserializeNBT(provider, nbt);
        return body;
    }
}
