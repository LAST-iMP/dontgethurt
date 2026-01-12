
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;

public class Head extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> HEAD_CONDITIONS;

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public float getVitalityWeight() {
        return 1f;
    }

    @Override
    public String getShortID() {
        return "E3CC1481-FF2D-4F65-AFCC";
    }

    @Override
    public Component getComponent() {
        return Component.literal("头部");
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (HEAD_CONDITIONS == null) {
            HEAD_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            HEAD_CONDITIONS.addAll(uniqueConditions);
        }
        return HEAD_CONDITIONS;
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleWithdraw(health);
        this.handleTraumaticShock(health);
        this.handleBrainDamage(health);
        this.handleComa(health);
        return this;
    }

    @Override
    public int slowDownLevel(HealthCapability health) {
        var slowLevel = super.slowDownLevel(health);
        if (this.getConditionValue(WITHDRAW) > 0.2f)
            slowLevel += 2;
        if (this.getConditionValue(WITHDRAW) > 0.4f)
            slowLevel += 4;
        if (this.getConditionValue(WITHDRAW) > 0.8f)
            slowLevel += 4;
        return slowLevel;
    }

    @Override
    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        var loss = super.updateVitalityLost(health, entity);
        loss += this.getVitalityWeight() * this.getConditionValue(BRAIN_DAMAGE);
        return loss;
    }

    @Override
    public float fractThreshold () {
        return Config.baseFractureThreshold + 0.2f;
    }

    private void handleWithdraw(HealthCapability health) {
        if (!this.abnormal(WITHDRAW)) return;

        if (this.getConditionValue(WITHDRAW) > health.getComponent(BLOOD).getConditionValue(OPIATE_ADDICTED))
            this.healing(WITHDRAW, -BodyCondition.get(WITHDRAW).healingSpeed() * DELTA);
    }

    private void handleTraumaticShock(HealthCapability health) {
        if (!this.abnormal(TRAUMATIC_SHOCK)) return;

        var value = this.getConditionValue(TRAUMATIC_SHOCK);
        if (value > 0.3f)
            health.getComponent(TORSO).injury(RESPIRATORY_ARREST, BodyCondition.get(RESPIRATORY_ARREST).maxValue());
        if (value > 0.1f)
            this.injury(BRAIN_DAMAGE, value * 0.01f * DELTA);
    }

    private void handleBrainDamage(HealthCapability health) {
        //起因
        var blood = health.getComponent(BLOOD);
        if (this.abnormal(FRACTURE) && !this.isBandaged() && !this.isBadBandaged()) {
            this.injury(BRAIN_DAMAGE, 0.001f * DELTA);
        }
        if (blood.getConditionValue(OXYGEN) > 0.1f) {
            this.injury(BRAIN_DAMAGE, blood.getConditionValue(OXYGEN) * 0.01f * DELTA);
        }
        if (this.getConditionValue(TRAUMATIC_SHOCK) > 0.1f) {
            this.injury(BRAIN_DAMAGE, this.getConditionValue(TRAUMATIC_SHOCK) * 0.01f * DELTA);
        }
        if (blood.abnormal(SEPSIS)) {
            this.injury(BRAIN_DAMAGE, 0.001f * 4 * blood.getConditionValue(SEPSIS) * DELTA);
        }
        //更新 自然回复
    }

    private void handleComa(HealthCapability health) {
        var torso = health.getComponent(TORSO);
        if (torso.abnormal(HEARTRATE_STOP)) {
            this.injury(COMA, BodyCondition.get(COMA).maxValue());
        }
    }
}
