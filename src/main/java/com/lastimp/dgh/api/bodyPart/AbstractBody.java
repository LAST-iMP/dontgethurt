/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
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

    public void updateDisplayValue(PlayerHealthCapability health) {
        for (BodyCondition condition : this.getBodyConditions()) {
            ConditionState state = this.getCondition(condition);
            if (state.getTickCounter() > 20) continue;
            float weight = this.easeOutQuart(state.getTickCounter());
            float displayValue = state.getLastDisplayValue() * (1 - weight) + state.getValue() * weight;
            state.setDisplayValue(displayValue);
            state.setTickCounter(state.getTickCounter() + 1);
            if (state.getTickCounter() > 20) {
                state.setLastDisplayValue(state.getValue());
            }
        }
    }

    protected float easeOutQuart(int tick) {
        return ConditionState.EASE_OUT_QUART[tick];
    }

    protected boolean abnormalWithHidden(BodyCondition condition) {
        ConditionState state = this.getCondition(condition);
        return condition.abnormal(state.getValue()) || condition.abnormal(state.getHiddenValue());
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
