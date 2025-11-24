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

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

public class ConditionState implements INBTSerializable<CompoundTag> {
    public static final int MAX_TICK = 20;
    public static final float[] EASE_OUT_QUART = {
        0.0f,       0.18549375f,    0.3439f,    0.47799375f,    0.59034f,   0.68359375f,    0.7599f,    0.82149375f,
        0.8704f,    0.90849375f,    0.9375f,    0.95899375f,    0.9744f,    0.98499375f,    0.9919f,    0.99609375f,
        0.9984f,    0.99949375f,    0.9999f,    0.99999375f,    1.0f
    };

    private boolean isInjury;
    private float lastDisplayValue;
    private float displayValue;
    private int tickCounter;
    private float value;
    private float hiddenValue;
    private int stateLevel;

    public ConditionState() {
        this(true, 0f, 0f, 0);
    }

    public ConditionState(float defaultValue) {
        this(true, defaultValue, 0f, 0);
    }

    public ConditionState(boolean isInjury, float value, float hiddenValue, int stateLevel) {
        this.build(isInjury, value, value, 21, value, hiddenValue, stateLevel);
    }

    public void copy(ConditionState state) {
        this.build(state.isInjury, state.lastDisplayValue, state.displayValue, state.tickCounter, state.value, state.hiddenValue, state.stateLevel);
    }

    private void build(boolean isInjury, float lastDisplayValue, float displayValue, int tickCounter, float value, float hiddenValue, int stateLevel) {
        this.isInjury = isInjury;
        this.lastDisplayValue = lastDisplayValue;
        this.displayValue = displayValue;
        this.tickCounter = tickCounter;
        this.value = value;
        this.hiddenValue = hiddenValue;
        this.stateLevel = stateLevel;
    }

    public float getDisplayValue() {
        return displayValue;
    }

    public float getHiddenValue() {
        return hiddenValue;
    }

    public float getTotalValue() {
        return hiddenValue + value;
    }

    public void setHiddenValue(float hiddenValue) {
        this.hiddenValue = hiddenValue;
    }

    public boolean isInjury() {
        return isInjury;
    }

    public void setInjury(boolean injury) {
        isInjury = injury;
    }

    public int getStateLevel() {
        return stateLevel;
    }

    public void setStateLevel(int stateLevel) {
        this.stateLevel = stateLevel;
    }

    public void tick() {
        if (this.tickCounter >= 20) {
            this.displayValue = this.value;
            return;
        }

        this.tickCounter++;
        float weight = ConditionState.EASE_OUT_QUART[this.tickCounter];
        this.displayValue = this.lastDisplayValue * (1 - weight) + this.value * weight;
        if (this.tickCounter >= 20) {
            this.displayValue = this.value;
            this.lastDisplayValue = this.displayValue;
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.lastDisplayValue = this.displayValue;
        this.value = value;
        this.tickCounter = 0;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isInjury", this.isInjury);
        tag.putFloat("lastDisplayValue", this.lastDisplayValue);
        tag.putFloat("displayValue", this.displayValue);
        tag.putInt("tickCounter", this.tickCounter);
        tag.putFloat("value", this.value);
        tag.putFloat("hiddenValue", this.hiddenValue);
        tag.putInt("stateLevel", this.stateLevel);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        this.build(
                tag.getBoolean("isInjury"),
                tag.getFloat("lastDisplayValue"),
                tag.getFloat("displayValue"),
                tag.getInt("tickCounter"),
                tag.getFloat("value"),
                tag.getFloat("hiddenValue"),
                tag.getInt("stateLevel")
        );
    }
}
