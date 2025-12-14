
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Blood extends AbstractBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> BLOOD_CONDITIONS;

    public Blood() {
        super();
    }

    public Blood(Void v) {
        this();
    }

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (BLOOD_CONDITIONS == null) {
            BLOOD_CONDITIONS = new ArrayList<>(uniqueConditions);
        }
        return BLOOD_CONDITIONS;
    }

    @Override
    public float getVitalityWeight() {
        return 1;
    }

    @Override
    public String getShortID() {
        return "C264AB58-CC16-425E-B12D";
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        this.handleBloodVolume(health);
        this.handleOpiateAddicted(health);
        this.handleOxygen(health, entity);
        return this;
    }

    @Override
    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        float lost = 0;
        if (this.abnormal(OPIATE_OVERDOSE))
            lost += Mth.clamp(this.getConditionValue(OPIATE_OVERDOSE) - 0.5f, 0.0f, 0.5f);
        return lost;
    }

    @Override
    public int slowDownLevel(HealthCapability health) {
        return this.getConditionValue(OPIATE_OVERDOSE) < 0.5f? 0 : 8;
    }

    private void handleBloodVolume(HealthCapability health) {
        if (!this.abnormalWithHidden(BLOOD_LOSS)) return;
        var value = this.getConditionValue(BLOOD_LOSS);
        if (value > 0.4f) {
            if (this.getConditionValue(OXYGEN) < value)
                this.setConditionValue(OXYGEN, (value - 0.4f) / 0.6f);
        }

        if (this.isBleeding(health)) return;
        var bloodLoss = BodyCondition.get(BLOOD_LOSS);
        if (value > bloodLoss.defaultValue() + EPS)
            this.healing(BLOOD_LOSS, -bloodLoss.healingSpeed() * DELTA);
    }

    private boolean isBleeding(HealthCapability health) {
        for (var component : BodyComponents.VISIBLE_BODIES) {
            var body = health.getComponent(component);
            if (body.abnormal(BLEED))
                return true;
        }
        return false;
    }

    private void handleOxygen(HealthCapability health, LivingEntity entity) {
        if (!this.abnormal(OXYGEN)) return;

        if (this.getConditionValue(OXYGEN) > 0.1f)
            health.getComponent(HEAD).injury(BRAIN_DAMAGE, this.getConditionValue(OXYGEN) * 0.01f * DELTA);
        if (!health.getComponent(TORSO).abnormal(RESPIRATORY_ARREST) && entity.getAirSupply() >= 2) {
            var oxygen = BodyCondition.get(OXYGEN);
            this.healing(OXYGEN, -oxygen.healingSpeed() * DELTA);
            entity.setAirSupply(entity.getAirSupply() - 1);
        }
    }

    private void handleOpiateAddicted(HealthCapability health) {
        if (!this.abnormal(OPIATE_ADDICTED)) return;

        Head head = (Head) health.getComponent(HEAD);
        if (head.getConditionValue(WITHDRAW) < this.getConditionValue(OPIATE_ADDICTED) && !health.getComponent(TORSO).abnormal(ANALGESIA))
            head.injury(WITHDRAW, this.getConditionValue(OPIATE_ADDICTED) * DELTA * Config.withdraw_ratio);
    }
}
