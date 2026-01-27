package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.menu.component.DynamicValidItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;

public abstract class AbstractBody implements INBTSerializable<CompoundTag> {
    public static final int ORGAN_1_START = 0;
    public static final int ORGAN_1_END = 12;
    public static final int ORGAN_2_START = 12;
    public static final int ORGAN_2_END = 24;
    public static final int ORGAN_3_START = 24;
    public static final int ORGAN_3_END = 36;
    private final DynamicValidItemHandler organ = new DynamicValidItemHandler(36, 64);

    private final HashMap<ResourceLocation, ConditionState> state = new HashMap<>();
    private static final HashMap<String, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> handlers = new LinkedHashMap<>();

    public AbstractBody() {
        for (var condition : this.getBodyConditions()) {
            state.put(condition, new ConditionState(BodyCondition.get(condition).defaultValue()));
        }
        this.initOrgan();
        this.addOriginOrgan(null, true);
    }

    public static void addHandler(String ID, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler) {
        handlers.put(ID, handler);
    }

    public abstract List<ResourceLocation> getBodyConditions();

    public abstract float getVitalityWeight();

    public abstract String getShortID();

    public abstract Component getComponent();

    public abstract void addOriginOrgan(LivingEntity livingEntity, boolean newEntity);

    public DynamicValidItemHandler organ() {
        return this.organ;
    }

    protected void initOrgan() {
        this.organ.addAllowed(ModTags.ORGAN);
    }

    public int countOrganMatch(TagKey<Item> key) {
        int count = 0;
        for (var organ : this.organ) {
            if (organ.is(key)) count++;
        }
        return count;
    }

    protected void insertOrganIfMissing(int start, int end, LivingEntity livingEntity, TagKey<Item> tag, ItemStack defaultItem) {
        insertOrganIfMissing(start, end, 1, livingEntity, tag, defaultItem);
    }

    protected void insertOrganIfMissing(int start, int end, int require, LivingEntity livingEntity, TagKey<Item> tag, ItemStack defaultItem) {
        for (int i = 0; i < require; i++) {
            if (this.countOrganMatch(tag) < i + 1 && start + i < end) {
                var remaining = this.organ().insertTo(start + i, end, defaultItem);
                if (!remaining.isEmpty() && livingEntity != null) {
                    Utils.drop(remaining, livingEntity);
                }
            }
        }
    }

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
        this.updateOrgan(health, entity);
        this.selfHealing();
        this.updateDisplayValue(health);
        return this;
    }

    private void updateOrgan(HealthCapability health, LivingEntity entity) {
        for (int i = 0; i < this.organ.getSlots(); i++) {
            var stack = this.organ().getStackInSlot(i);
            if (stack.isEmpty()) continue;
            stack = ((AbstractOrgan)stack.getItem()).update(stack, health, this, entity);
            this.organ().setStackInSlot(i, stack);
        }
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
        return this.abnormal(key) || this.abnormalOnlyHidden(key);
    }

    public boolean abnormalOnlyHidden(ResourceLocation key) {
        if (!this.getBodyConditions().contains(key)) return false;
        var condition = BodyCondition.get(key);
        return condition.abnormal(this.getCondition(key).getHiddenValue());
    }

    public boolean abnormal(ResourceLocation key) {
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

    public void healingAll(boolean healPain) {
        this.getBodyConditions().forEach(key -> {
            var condition = BodyCondition.get(key);
            if (condition.isInjury() || (condition.isPain() && healPain)) {
                this.setConditionValue(key, condition.defaultValue());
                this.setConditionHidden(key, condition.defaultValue());
            }
        });
    }

    public CompoundTag deathSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("organ", this.organ.serializeNBT());
        return tag;
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<ResourceLocation, ConditionState> e : state.entrySet()) {
            var key = e.getKey();
            var state = e.getValue();
            if (state.isDefault(BodyCondition.get(key))) continue;
            tag.put(key.toString(), state.serializeNBT());
        }
        tag.put("organ", this.organ.serializeNBT());
        return tag;
    }

    public void respawnDeserializeNBT(CompoundTag nbt) {
        this.organ.deserializeNBT(nbt.getCompound("organ"));
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        for (var key : this.getBodyConditions()) {
            if (nbt.contains(key.toString())) {
                state.get(key).deserializeNBT(nbt.getCompound(key.toString()));
            } else {
                state.put(key, new ConditionState(BodyCondition.get(key).defaultValue()));
            }
        }
        this.organ.deserializeNBT(nbt.getCompound("organ"));
    }

    public static <T extends AbstractBody> AbstractBody buildFromNBT(CompoundTag nbt, Supplier<T> constructor) {
        T body = constructor.get();
        body.deserializeNBT(nbt);
        return body;
    }
}
