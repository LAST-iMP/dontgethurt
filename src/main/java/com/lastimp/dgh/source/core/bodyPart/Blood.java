
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModEffects;
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
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

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
        return "blood";
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleBloodVolume(health);
        this.handleOpiateAddicted(health, entity);
        this.handleWithdraw(health);
        this.handleOxygen(health, entity);
        this.handlePressure(health);
        this.handleInfection(health);
        this.handleSepsis(health);
        this.handleCombatStimulant(health, entity);
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
        boolean oxygenLost = false;
        var bloodLoss = this.getConditionValue(BLOOD_LOSS);
        if (bloodLoss > 0.4) {
            if (this.getConditionValue(OXYGEN) < (bloodLoss - 0.4f)) {
                this.setConditionValue(OXYGEN, (bloodLoss - 0.4f) / 0.6f);
                oxygenLost = true;
            }
        }
        var bloodPressure = this.getConditionValue(BLOOD_PRESSURE);
        if (bloodPressure < 0.7) {
            if (this.getConditionValue(OXYGEN) < (0.7 - bloodPressure)) {
                this.injury(OXYGEN, 0.5f * BodyCondition.get(OXYGEN).healingSpeed() * DELTA);
                oxygenLost = true;
            }
        }
        var torso = health.getComponent(TORSO);
        if (torso.abnormal(HEARTRATE_STOP)) {
            this.injury(OXYGEN, BodyCondition.get(OXYGEN).healingSpeed() * DELTA);
            oxygenLost = true;
        }
        if (!oxygenLost && !torso.abnormal(RESPIRATORY_ARREST) && torso.getConditionValue(PNEUMOTHORAX) < 0.1 && entity.getAirSupply() >= 2) {
            this.healing(OXYGEN, -BodyCondition.get(OXYGEN).healingSpeed() * DELTA);
            entity.setAirSupply(entity.getAirSupply() - 1);
        }
    }

    private void handleOpiateAddicted(HealthCapability health, LivingEntity entity) {
        if (entity.hasEffect(ModEffects.COMBAT_STIMULANT_EFFECT)) {
            this.injury(OPIATE_ADDICTED, DELTA * 0.0026f);
        }
    }

    private void handleWithdraw(HealthCapability health) {
        if (this.abnormal(OPIATE_ADDICTED)) {
            Head head = (Head) health.getComponent(HEAD);
            if (head.getConditionValue(WITHDRAW) < this.getConditionValue(OPIATE_ADDICTED) && !health.getComponent(TORSO).abnormal(ANALGESIA))
                head.injury(WITHDRAW, this.getConditionValue(OPIATE_ADDICTED) * DELTA * Config.withdraw_ratio);
        }
    }

    private void handlePressure(HealthCapability health) {
        var torso = health.getComponent(TORSO);
        if (torso.abnormal(HEARTRATE_IRREGULAR) && this.getConditionValue(BLOOD_PRESSURE) > 1.0 - torso.getConditionValue(HEARTRATE_IRREGULAR)) {
            this.injury(BLOOD_PRESSURE, -0.05f * DELTA);
        } else if (torso.abnormal(HEARTRATE_STOP)) {
            this.setConditionValue(BLOOD_PRESSURE, BodyCondition.get(BLOOD_PRESSURE).minValue());
        } else if (this.abnormal(BLOOD_PRESSURE)) {
            int factor = (this.getConditionValue(BLOOD_PRESSURE) > BodyCondition.get(BLOOD_PRESSURE).defaultValue()) ? -1 : 1;
            this.healing(BLOOD_PRESSURE, factor * BodyCondition.get(BLOOD_PRESSURE).healingSpeed() * DELTA);
        }
    }

    private void handleInfection(HealthCapability health) {
        float infection = 0;
        float gangrene = 0;
        for (var component : BodyComponents.VISIBLE_BODIES) {
            AbstractVisibleBody body = (AbstractVisibleBody) health.getComponent(component);
            infection += body.getConditionValue(INFECTION);
            if (body instanceof AbstractExtremities extremities)
                gangrene += extremities.getConditionValue(GANGRENE);
        }
        Blood blood = (Blood) health.getComponent(BLOOD);
        if (infection > 0.5f) {
            blood.addConditionValue(SEPSIS, 2 * infection * BodyCondition.get(SEPSIS).healingSpeed() * DELTA);
        }
        if (gangrene > 0.15f) {
            blood.addConditionValue(SEPSIS, 2 * gangrene * BodyCondition.get(SEPSIS).healingSpeed() * DELTA);
        }
    }

    private void handleSepsis(HealthCapability health) {
        if (this.abnormal(ANTIBIOTICS) && this.getConditionValue(OXYGEN) < 0.3f && this.getConditionValue(BLOOD_PRESSURE) > 0.7f) {
            this.healing(SEPSIS, -BodyCondition.get(SEPSIS).healingSpeed() * DELTA * 2);
        }
    }

    private void handleCombatStimulant(HealthCapability health, LivingEntity entity) {
        if (!entity.hasEffect(ModEffects.COMBAT_STIMULANT_EFFECT)) return;
        this.healing(BLOOD_LOSS, -0.02f * DELTA);
        var head = health.getComponent(HEAD);
        head.healing(WITHDRAW, -0.02f * DELTA);
    }
}
