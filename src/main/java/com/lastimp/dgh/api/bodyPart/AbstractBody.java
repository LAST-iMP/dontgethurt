
package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;

public abstract class AbstractBody implements INBTSerializable<CompoundTag> {

    private final HashMap<ResourceLocation, ConditionState> state = new HashMap<>();

    public AbstractBody() {
        for (var condition : this.getBodyConditions()) {
            state.put(condition, new ConditionState(BodyCondition.get(condition).defaultValue()));
        }
    }

    public abstract List<ResourceLocation> getBodyConditions();

    public abstract float getVitalityWeight();

    public abstract String getShortID();

    public ConditionState getCondition(ResourceLocation key) {
        return state.get(key);
    }

    public float getConditionValue(ResourceLocation key) {
        return this.getCondition(key).getValue();
    }

    public void setConditionValue(ResourceLocation key, float value) {
        ConditionState state = this.state.get(key);
        BodyCondition condition = BodyCondition.get(key);
        state.setValue(Mth.clamp(value, condition.minValue(), condition.maxValue()));
    }

    public void addConditionValue(ResourceLocation key, float value) {
        float newValue = this.getConditionValue(key) + value;
        this.setConditionValue(key, newValue);
    }

    public float getConditionHidden(ResourceLocation key) {
        return this.getCondition(key).getHiddenValue();
    }

    public void setConditionHidden(ResourceLocation key, float value) {
        ConditionState state = this.state.get(key);
        BodyCondition condition = BodyCondition.get(key);
        state.setHiddenValue(Mth.clamp(value, condition.minValue(), condition.maxValue()));
    }

    public void addConditionHidden(ResourceLocation key, float value) {
        float newValue = this.getConditionHidden(key) + value;
        this.setConditionHidden(key, newValue);
    }

    public void injury(ResourceLocation key, float value) {
        this.addConditionValue(key, value);
    }

    public void injuryHidden(ResourceLocation key, float value) {
        this.addConditionHidden(key, value);
    }

    public void healing(ResourceLocation key, float value) {
        this.addConditionValue(key, value);
    }

    public void healingHidden(ResourceLocation key, float value) {
        this.addConditionHidden(key, value);
    }

    public abstract AbstractBody update(HealthCapability health, LivingEntity entity);

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

    protected List<ResourceLocation> getNoHealingConditions() {
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

    public boolean abnormalWithHidden(ResourceLocation key) {
        var state = this.getCondition(key);
        var condition = BodyCondition.get(key);
        return condition.abnormal(state.getValue()) || condition.abnormal(state.getHiddenValue());
    }

    public boolean abnormalOnlyHidden(ResourceLocation key) {
        var state = this.getCondition(key);
        var condition = BodyCondition.get(key);
        return condition.abnormal(state.getHiddenValue());
    }

    public boolean abnormal(ResourceLocation key) {
        var condition = BodyCondition.get(key);
        return condition.abnormal(this.getConditionValue(key));
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<ResourceLocation, ConditionState> e : state.entrySet()) {
            tag.put(e.getKey().toString(), e.getValue().serializeNBT(provider));
        }
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        for (var key : this.getBodyConditions()) {
            if (nbt.contains(key.toString())) {
                state.get(key).deserializeNBT(provider, nbt.getCompound(key.toString()));
            } else {
                state.put(key, new ConditionState(BodyCondition.get(key).defaultValue()));
            }
        }
    }

    public static <T extends AbstractBody> AbstractBody buildFromNBT(HolderLookup.Provider provider, CompoundTag nbt, Function<Void, T> constructor) {
        T body = constructor.apply(null);
        body.deserializeNBT(provider, nbt);
        return body;
    }
}
