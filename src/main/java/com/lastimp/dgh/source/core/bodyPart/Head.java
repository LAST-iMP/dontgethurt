
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Head extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> HEAD_CONDITIONS;

    public Head() {
        super();
    }

    public Head(Void v) {
        this();
    }

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

}
