
package com.lastimp.dgh.api.bodyPart;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import static com.lastimp.dgh.DontGetHurt.EPS;

public class ConditionState implements ValueIOSerializable {
    public static final int MAX_TICK = 20;
    public static final float[] EASE_OUT_QUART = {
        0.0f,       0.18549375f,    0.3439f,    0.47799375f,    0.59034f,   0.68359375f,    0.7599f,    0.82149375f,
        0.8704f,    0.90849375f,    0.9375f,    0.95899375f,    0.9744f,    0.98499375f,    0.9919f,    0.99609375f,
        0.9984f,    0.99949375f,    0.9999f,    0.99999375f,    1.0f
    };

    private float lastDisplayValue;
    private float displayValue;
    private int tickCounter;
    private float value;
    private float hiddenValue;

    public ConditionState(float value) {
        this.build(value, value, 21, value, value);
    }

    private void build(float lastDisplayValue, float displayValue, int tickCounter, float value, float hiddenValue) {
        this.lastDisplayValue = lastDisplayValue;
        this.displayValue = displayValue;
        this.tickCounter = tickCounter;
        this.value = value;
        this.hiddenValue = hiddenValue;
    }

    public float getDisplayValue() {
        return Math.min(2.0f, displayValue);
    }

    public float getHiddenValue() {
        return hiddenValue;
    }

    public float getTotalValue() {
        return hiddenValue + value;
    }

    protected void setHiddenValue(float hiddenValue) {
        this.hiddenValue = hiddenValue;
    }

    protected void tick() {
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

    protected void setValue(float value) {
        this.lastDisplayValue = this.displayValue;
        this.value = value;
        this.tickCounter = 0;
    }

    public boolean isDefault(BodyCondition condition) {
        if (Math.abs(this.value - condition.defaultValue()) > EPS) return false;
        if (Math.abs(this.hiddenValue) > EPS) return false;
        return this.tickCounter >= 20;
    }

    @Override
    public void serialize(ValueOutput valueOutput) {
        valueOutput.putFloat("lastDisplayValue", this.lastDisplayValue);
        valueOutput.putFloat("displayValue", this.displayValue);
        valueOutput.putInt("tickCounter", this.tickCounter);
        valueOutput.putFloat("value", this.value);
        valueOutput.putFloat("hiddenValue", this.hiddenValue);
    }

    @Override
    public void deserialize(ValueInput valueInput) {
        this.build(
                valueInput.getFloatOr("lastDisplayValue", 0f),
                valueInput.getFloatOr("displayValue", 0f),
                valueInput.getIntOr("tickCounter", 21),
                valueInput.getFloatOr("value", 0f),
                valueInput.getFloatOr("hiddenValue", 0f)
        );
    }
}
