
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.api.enums.BodyComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.function.Predicate;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class WholeBody implements INBTSerializable<CompoundTag> {
    private final HashMap<BodyComponents, AbstractBody> components = new HashMap<>();

    public WholeBody() {
        components.put(LEFT_ARM, new LeftArm());
        components.put(RIGHT_ARM, new RightArm());
        components.put(LEFT_LEG, new LeftLeg());
        components.put(RIGHT_LEG, new RightLeg());
        components.put(HEAD, new Head());
        components.put(TORSO, new Torso());
        components.put(BLOOD, new Blood());
    }

    public AbstractBody getComponent(BodyComponents component) {
        return components.get(component);
    }

    public boolean anyMatch(Predicate<? super AbstractBody> predicate) {
        return this.components.values().stream().anyMatch(predicate);
    }

    @SuppressWarnings("unchecked")
    public boolean anyVisibleMatch(Predicate<? super AbstractVisibleBody> predicate) {
        return VISIBLE_BODIES.stream().map(this::getComponent).anyMatch((Predicate<? super AbstractBody>) predicate);
    }

    public boolean isInfected() {
        return this.anyVisibleMatch(AbstractVisibleBody::isInfected);
    }

    public boolean abnormal() {
        return this.anyMatch(AbstractBody::abnormal);
    }

    public void update(HealthCapability health, LivingEntity entity) {
        this.components.values().forEach(body -> body.updatePre(health, entity).update(health, entity).updatePost(health, entity));
    }

    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        float lost = 0;
        for (var body : this.components.values()) {
            var bodyLost = body.updateVitalityLost(health, entity);
            if (!(body instanceof Head) && Config.limited_body_part_vitality_lost)
                bodyLost = Math.min(bodyLost, 1);
            bodyLost *= body.getVitalityWeight();
            lost += bodyLost;
        }
        return lost;
    }

    public int slowDownLevel(HealthCapability health) {
        int slowDown = 0;
        for (var body : this.components.values()) {
            slowDown += body.slowDownLevel(health);
        }
        return Mth.clamp(slowDown, 0, 19);
    }

    public void addOriginOrgan(LivingEntity livingEntity, boolean newEntity) {
        VISIBLE_BODIES.stream().map(this::getComponent).forEach(body -> body.addOriginOrgan(livingEntity, newEntity));
    }

    public void healingAll(boolean healPain) {
        this.components.values().forEach(body -> body.healingAll(healPain));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        this.components.keySet().forEach(key -> tag.put(key.name(), this.getComponent(key).serializeNBT()));
        return tag;
    }

    public CompoundTag deathSerializeNBT() {
        CompoundTag tag = new CompoundTag();
        this.components.keySet().forEach(key -> tag.put(key.name(), this.getComponent(key).deathSerializeNBT()));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.components.keySet().forEach(key -> components.get(key).deserializeNBT(nbt.getCompound(key.name())));
    }

    public void respawnDeserializeNBT(CompoundTag nbt) {
        this.components.keySet().forEach(key -> components.get(key).respawnDeserializeNBT(nbt.getCompound(key.name())));
    }
}
