package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModItems;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.TORSO;

public abstract class AbstractExtremities extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> EXTREMITY_CONDITIONS;
    private int tourniquetTick = 0;

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public float getVitalityWeight() {
        return 0.4f;
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (EXTREMITY_CONDITIONS == null) {
            EXTREMITY_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            EXTREMITY_CONDITIONS.addAll(uniqueConditions);
        }
        return EXTREMITY_CONDITIONS;
    }

    @Override
    public boolean canHurtBone() {
        if (this.abnormalWithHidden(TRAUMATIC_AMPUTATION)) return false;
        if (this.abnormalWithHidden(SURGICAL_AMPUTATION)) return false;
        return super.canHurtBone();
    }

    @Override
    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        var loss = super.updateVitalityLost(health, entity);
        if (this.abnormal(SURGICAL_AMPUTATION) || this.abnormal(TRAUMATIC_AMPUTATION))
            loss = Math.max(0.5f, loss);
        return loss;
    }

    @Override
    public void addOriginOrgan(LivingEntity livingEntity, boolean newEntity) {
        if (!newEntity) return;
        this.insertOrganIfMissing(0, ORGAN_1_END, livingEntity, ModTags.NEURO, ModItems.NEURO.get().getDefaultInstance());
        this.insertOrganIfMissing(1, ORGAN_1_END, livingEntity, ModTags.SKIN, ModItems.SKIN.get().getDefaultInstance());
        this.insertOrganIfMissing(2, ORGAN_1_END, livingEntity, ModTags.MUSCLE, ModItems.MUSCLE.get().getDefaultInstance());
    }

    @Override
    public int organ1BaseLevel() {
        return 2;
    }

    public boolean available(HealthCapability health) {
        boolean available = this.isBandaged() || this.isBadBandaged() || this.abnormal(CLAMP_PLATE) || !this.abnormal(DISLOCATION);
        available &= this.abnormal(PLASTER_CAST) || this.abnormal(CLAMP_PLATE) || !this.abnormalWithHidden(FRACTURE);
        available |= health.getComponent(TORSO).abnormal(ANALGESIA);
        available &= !this.abnormalWithHidden(SAWED_BONES);
        available &= (!this.abnormal(TRAUMATIC_AMPUTATION) && !this.abnormal(SURGICAL_AMPUTATION));
        return available;
    }

    public static boolean available(HealthCapability health, BodyComponents components) {
        if (!BodyComponents.EXTREMITIES.contains(components)) return true;
        AbstractExtremities body = (AbstractExtremities) health.getComponent(components);
        return body.available(health);
    }

    public boolean visible() {
        return (!this.abnormal(TRAUMATIC_AMPUTATION) && !this.abnormal(SURGICAL_AMPUTATION));
    }

    public static boolean visible(HealthCapability health, BodyComponents components) {
        if (!BodyComponents.EXTREMITIES.contains(components)) return true;
        AbstractExtremities body = (AbstractExtremities) health.getComponent(components);
        return body.visible();
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleDislocation(health);
        this.handleGangrene(health);
        return this;
    }

    private void handleDislocation(HealthCapability health) {
        if (!this.abnormal(DISLOCATION)) return;

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!this.abnormal(CLAMP_PLATE) && !torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, ConditionAccessor.get(INTENSE_PAIN).maxValue());
        }
    }

    private void handleGangrene(HealthCapability health) {
        if (this.abnormal(CLAMPED_ARTERIES)) {
            this.tourniquetTick++;
        } else {
            this.tourniquetTick = 0;
        }
        if (this.tourniquetTick >= 4800) {
            this.injury(GANGRENE, ConditionAccessor.get(GANGRENE).healingSpeed() * 2 * Utils.DELTA);
        }
    }
}
