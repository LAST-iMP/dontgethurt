
package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;

public abstract class AbstractBody implements ValueIOSerializable {

    private final HashMap<Identifier, ConditionState> state = new HashMap<>();
    private static final HashMap<String, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> handlers = new LinkedHashMap<>();

    public AbstractBody() {
        for (var condition : this.getBodyConditions()) {
            state.put(condition, new ConditionState(BodyCondition.get(condition).defaultValue()));
        }
    }

    public static void addHandler(String ID, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler) {
        handlers.put(ID, handler);
    }

    public abstract List<Identifier> getBodyConditions();

    public abstract float getVitalityWeight();

    public abstract String getShortID();

    public abstract Component getComponent();

    public ConditionState getCondition(Identifier key) {
        return state.get(key);
    }

    public float getConditionValue(Identifier key) {
        return this.getCondition(key).getValue();
    }

    public void setConditionValue(Identifier key, float value) {
        ConditionState state = this.state.get(key);
        BodyCondition condition = BodyCondition.get(key);
        state.setValue(Mth.clamp(value, condition.minValue(), condition.maxValue()));
    }

    public void addConditionValue(Identifier key, float value) {
        float newValue = this.getConditionValue(key) + value;
        this.setConditionValue(key, newValue);
    }

    public float getConditionHidden(Identifier key) {
        return this.getCondition(key).getHiddenValue();
    }

    public void setConditionHidden(Identifier key, float value) {
        ConditionState state = this.state.get(key);
        BodyCondition condition = BodyCondition.get(key);
        state.setHiddenValue(Mth.clamp(value, condition.minValue(), condition.maxValue()));
    }

    public void addConditionHidden(Identifier key, float value) {
        float newValue = this.getConditionHidden(key) + value;
        this.setConditionHidden(key, newValue);
    }

    public void injury(Identifier key, float value) {
        this.addConditionValue(key, value);
    }

    public void injuryHidden(Identifier key, float value) {
        this.addConditionHidden(key, value);
    }

    public void healing(Identifier key, float value) {
        this.addConditionValue(key, value);
    }

    public void healingHidden(Identifier key, float value) {
        this.addConditionHidden(key, value);
    }

    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        for (var handler : handlers.values()) {
            handler.accept(Triple.of(health, entity, this));
        }
        return this;
    }

    public AbstractBody updatePre(HealthCapability health, LivingEntity entity) {
        return this;
    }

    public AbstractBody updatePost(HealthCapability health, LivingEntity entity) {
        this.selfHealing();
        this.updateDisplayValue(health);
        return this;
    }

    public abstract float updateVitalityLost(HealthCapability health, LivingEntity entity);

    public abstract int slowDownLevel(HealthCapability health);

    private void updateDisplayValue(HealthCapability health) {
        for (var condition : this.getBodyConditions()) {
            ConditionState state = this.getCondition(condition);
            state.tick();
        }
    }

    protected List<Identifier> getNoHealingConditions() {
        return List.of();
    }

    private void selfHealing() {
        for (var key : this.getBodyConditions()) {
            if (!this.abnormal(key)) continue;
            if (this.getNoHealingConditions().contains(key)) continue;
            var condition = BodyCondition.get(key);
            if (this.getConditionValue(key) < condition.healingTS() + EPS)
                this.healing(key, - condition.healingSpeed() * DELTA);
        }
    }

    public boolean abnormalWithHidden(Identifier key) {
        return this.abnormal(key) || this.abnormalOnlyHidden(key);
    }

    public boolean abnormalOnlyHidden(Identifier key) {
        if (!this.getBodyConditions().contains(key)) return false;
        var condition = BodyCondition.get(key);
        return condition.abnormal(this.getCondition(key).getHiddenValue());
    }

    public boolean abnormal(Identifier key) {
        if (!this.getBodyConditions().contains(key)) return false;
        var condition = BodyCondition.get(key);
        return condition.abnormal(this.getConditionValue(key));
    }

    public boolean abnormal() {
        for (var key : this.state.keySet()) {
            var condition = BodyCondition.get(key);
            if (condition.isInjury() || condition.isPain())
                if (this.abnormalWithHidden(key)) return true;
        }
        return false;
    }

    @Override
    public void serialize(@NotNull ValueOutput valueOutput) {
        for (Map.Entry<Identifier, ConditionState> e : state.entrySet()) {
            var key = e.getKey();
            var state = e.getValue();
            if (state.isDefault(BodyCondition.get(key))) continue;
            valueOutput.putChild(key.toString(), state);
        }
    }

    @Override
    public void deserialize(@NotNull ValueInput valueInput) {
        for (var key : this.getBodyConditions()) {
            var newState = new ConditionState(BodyCondition.get(key).defaultValue());
            valueInput.child(key.toString()).ifPresent(newState::deserialize);
            state.put(key, newState);
        }
    }

    public static <T extends AbstractBody> AbstractBody buildFromNBT(@NotNull ValueInput valueInput, Supplier<T> constructor) {
        T body = constructor.get();
        body.deserialize(valueInput);
        return body;
    }
}
