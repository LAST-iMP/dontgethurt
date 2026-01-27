
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;

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
        return "head";
    }

    @Override
    public Component getComponent() {
        return Component.literal("头部");
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().setValidator((slot, stack) -> {
            if (stack.is(ModTags.ORGAN_HEAD)) return true;
            return false;
        });
    }

    @Override
    public void addOriginOrgan(LivingEntity livingEntity, boolean newEntity) {
        this.insertOrganIfMissing(0, ORGAN_1_END, livingEntity, ModTags.BRAIN, ModItems.BRAIN.get().getDefaultInstance());
        this.insertOrganIfMissing(1, ORGAN_1_END, livingEntity, ModTags.SPINAL_CORD, ModItems.SPINAL_CORD.get().getDefaultInstance());
        if (!newEntity)
            this.insertOrganIfMissing(2, ORGAN_1_END, livingEntity, ModTags.EYE, ModItems.EYE.get().getDefaultInstance());
        else
            this.insertOrganIfMissing(2, ORGAN_1_END, 2, livingEntity, ModTags.EYE, ModItems.EYE.get().getDefaultInstance());
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
        loss += this.getConditionValue(BRAIN_DAMAGE);
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
        float brain_damage = 0;
        var blood = health.getComponent(BLOOD);
        if (this.abnormal(FRACTURE) && !this.isBandaged() && !this.isBadBandaged()) {
            brain_damage += 0.001f;
        }
        if (blood.getConditionValue(OXYGEN) > 0.1f) {
            brain_damage += blood.getConditionValue(OXYGEN) * 0.01f;
        }
        if (this.getConditionValue(TRAUMATIC_SHOCK) > 0.1f) {
            brain_damage += this.getConditionValue(TRAUMATIC_SHOCK) * 0.01f;
        }
        if (blood.abnormal(SEPSIS)) {
            brain_damage += 0.001f * 4 * blood.getConditionValue(SEPSIS) * 0.01f;
        }
        if (this.countOrganMatch(ModTags.BRAIN) < 1) {
            brain_damage += 0.1f;
        }
        if (brain_damage > 0) {
            this.injury(BRAIN_DAMAGE, brain_damage * DELTA);
        }
    }

    private void handleComa(HealthCapability health) {
        var torso = health.getComponent(TORSO);
        if (torso.abnormal(HEARTRATE_STOP)) {
            this.injury(COMA, BodyCondition.get(COMA).maxValue());
        }
    }

}
