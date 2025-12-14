package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public abstract class AbstractExtremities extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> EXTREMITY_CONDITIONS;

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

    public boolean available(HealthCapability health) {
        boolean available = this.isBandaged() || this.isBadBandaged() || !this.abnormal(DISLOCATION);
        available &= this.abnormal(PLASTER_CAST) || !this.abnormalWithHidden(FRACTURE);
        available |= health.getComponent(TORSO).abnormal(ANALGESIA);
        available &= !this.abnormalWithHidden(SAWED_BONES);
        return available;
    }

    public static boolean available(HealthCapability health, BodyComponents components) {
        if (!BodyComponents.EXTREMITIES.contains(components)) return true;
        AbstractExtremities body = (AbstractExtremities) health.getComponent(components);
        return body.available(health);
    }


    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleDislocation(health);
        return this;
    }

    private void handleDislocation(HealthCapability health) {
        if (!this.abnormal(DISLOCATION)) return;

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, BodyCondition.get(INTENSE_PAIN).maxValue());
        }
    }
}
