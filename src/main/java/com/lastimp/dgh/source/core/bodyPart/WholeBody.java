
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class WholeBody extends AbstractBody {
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
        return component == WHOLE_BODY ? this : components.get(component);
    }

    public boolean isInfected() {
        for (var component : VISIBLE_BODIES) {
            if (((AbstractVisibleBody)this.getComponent(component)).isInfected()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Identifier> getBodyConditions() {
        return List.of();
    }

    @Override
    public float getVitalityWeight() {
        return 1;
    }

    @Override
    public String getShortID() {
        return "whole_body";
    }

    @Override
    public Component getComponent() {
        return Component.literal("全身");
    }

    public boolean abnormal() {
        for (var key : this.components.values()) {
            if (key.abnormal()) return true;
        }
        return false;
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        for (BodyComponents components : this.components.keySet()) {
            this.updateComponent(components, health, entity);
        }
        return this;
    }

    private void updateComponent(BodyComponents component, HealthCapability health, LivingEntity entity) {
        components.get(component).updatePre(health, entity).update(health, entity).updatePost(health, entity);
    }

    @Override
    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        float lost = 0;
        for (BodyComponents components : this.components.keySet()) {
            lost += this.getComponent(components).updateVitalityLost(health, entity);
        }
        return lost;
    }

    @Override
    public int slowDownLevel(HealthCapability health) {
        int slowDown = 0;
        for (var body : this.components.values()) {
            slowDown += body.slowDownLevel(health);
        }
        return Mth.clamp(slowDown, 0, 19);
    }

    @Override
    public void serialize(@NotNull ValueOutput valueOutput) {
        for (BodyComponents comp : components.keySet()) {
            valueOutput.putChild(comp.name(), components.get(comp));
        }
    }

    @Override
    public void deserialize(@NotNull ValueInput valueInput) {
        valueInput.child(LEFT_ARM.name()).ifPresent(nbt -> components.put(LEFT_ARM, AbstractBody.buildFromNBT(nbt, LeftArm::new)));
        valueInput.child(RIGHT_ARM.name()).ifPresent(nbt -> components.put(RIGHT_ARM, AbstractBody.buildFromNBT(nbt, RightArm::new)));
        valueInput.child(LEFT_LEG.name()).ifPresent(nbt -> components.put(LEFT_LEG, AbstractBody.buildFromNBT(nbt, LeftLeg::new)));
        valueInput.child(RIGHT_LEG.name()).ifPresent(nbt -> components.put(RIGHT_LEG, AbstractBody.buildFromNBT(nbt, RightLeg::new)));
        valueInput.child(HEAD.name()).ifPresent(nbt -> components.put(HEAD, AbstractBody.buildFromNBT(nbt, Head::new)));
        valueInput.child(TORSO.name()).ifPresent(nbt -> components.put(TORSO, AbstractBody.buildFromNBT(nbt, Torso::new)));
        valueInput.child(BLOOD.name()).ifPresent(nbt -> components.put(BLOOD, AbstractBody.buildFromNBT(nbt, Blood::new)));
    }
}
