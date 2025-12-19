
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class WholeBody extends AbstractBody {
    private final HashMap<BodyComponents, AbstractBody> components = new HashMap<>();
    private static List<ResourceLocation> WHOLE_BODY_CONDITIONS;

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

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (WHOLE_BODY_CONDITIONS == null) {
            WHOLE_BODY_CONDITIONS = List.of();
        }
        return WHOLE_BODY_CONDITIONS;
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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag wholeBody = super.serializeNBT(provider);
        CompoundTag tag = new CompoundTag();
        for (BodyComponents comp : components.keySet()) {
            tag.put(comp.name(), components.get(comp).serializeNBT(provider));
        }
        tag.put(WHOLE_BODY.name(), wholeBody);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        components.put(LEFT_ARM, AbstractBody.buildFromNBT(provider, nbt.getCompound(LEFT_ARM.name()), LeftArm::new));
        components.put(RIGHT_ARM, AbstractBody.buildFromNBT(provider, nbt.getCompound(RIGHT_ARM.name()), RightArm::new));
        components.put(LEFT_LEG, AbstractBody.buildFromNBT(provider, nbt.getCompound(LEFT_LEG.name()), LeftLeg::new));
        components.put(RIGHT_LEG, AbstractBody.buildFromNBT(provider, nbt.getCompound(RIGHT_LEG.name()), RightLeg::new));
        components.put(HEAD, AbstractBody.buildFromNBT(provider, nbt.getCompound(HEAD.name()), Head::new));
        components.put(TORSO, AbstractBody.buildFromNBT(provider, nbt.getCompound(TORSO.name()), Torso::new));
        components.put(BLOOD, AbstractBody.buildFromNBT(provider, nbt.getCompound(BLOOD.name()), Blood::new));
        super.deserializeNBT(provider, nbt.getCompound(WHOLE_BODY.name()));
    }
}
