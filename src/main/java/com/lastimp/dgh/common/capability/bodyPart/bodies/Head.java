
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

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
    public BodyComponents getBodyType() {
        return HEAD;
    }

    @Override
    public Component getComponent() {
        return Component.literal("头部");
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().addAllowed(ModTags.ORGAN_HEAD);
    }

    @Override
    public void addOriginOrgan(LivingEntity livingEntity, boolean newEntity) {
        this.insertOrganIfMissing(0, ORGAN_1_END, livingEntity, ModTags.SPINAL_CORD, ModItems.SPINAL_CORD.get().getDefaultInstance());
        this.insertOrganIfMissing(1, ORGAN_1_END, livingEntity, ModTags.BRAIN, ModItems.BRAIN.get().getDefaultInstance());
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
        if (this.getConditionValue(BodyCondition.WITHDRAW) > 0.2f)
            slowLevel += 2;
        if (this.getConditionValue(BodyCondition.WITHDRAW) > 0.4f)
            slowLevel += 4;
        if (this.getConditionValue(BodyCondition.WITHDRAW) > 0.8f)
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
        return PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD() + 0.2f;
    }

    @Override
    public int organ1BaseLevel() {
        return 4;
    }

    private void handleWithdraw(HealthCapability health) {
        if (!this.abnormal(BodyCondition.WITHDRAW)) return;

        if (this.getConditionValue(BodyCondition.WITHDRAW) > health.getComponent(BLOOD).getConditionValue(BodyCondition.OPIATE_ADDICTED))
            this.healing(BodyCondition.WITHDRAW, -ConditionAccessor.get(BodyCondition.WITHDRAW).healingSpeed() * Utils.DELTA);
    }

    private void handleTraumaticShock(HealthCapability health) {
        if (!this.abnormal(BodyCondition.TRAUMATIC_SHOCK)) return;

        var value = this.getConditionValue(BodyCondition.TRAUMATIC_SHOCK);
        if (value > 0.3f)
            health.getComponent(TORSO).injury(BodyCondition.RESPIRATORY_ARREST, ConditionAccessor.get(BodyCondition.RESPIRATORY_ARREST).maxValue());
        if (value > 0.1f)
            this.injury(BRAIN_DAMAGE, value * 0.01f * Utils.DELTA);
    }

    private void handleBrainDamage(HealthCapability health) {
        //起因
        float brain_damage = 0;
        var blood = health.getComponent(BLOOD);
        if (this.abnormal(BodyCondition.FRACTURE) && !this.isBandaged() && !this.isBadBandaged()) {
            brain_damage += 0.001f;
        }
        if (blood.getConditionValue(BodyCondition.OXYGEN) > 0.1f) {
            brain_damage += blood.getConditionValue(BodyCondition.OXYGEN) * 0.01f;
        }
        if (this.getConditionValue(BodyCondition.TRAUMATIC_SHOCK) > 0.1f) {
            brain_damage += this.getConditionValue(BodyCondition.TRAUMATIC_SHOCK) * 0.01f;
        }
        if (blood.abnormal(BodyCondition.SEPSIS)) {
            brain_damage += 0.001f * 4 * blood.getConditionValue(BodyCondition.SEPSIS) * 0.01f;
        }
        if (this.countOrganMatch(ModTags.BRAIN) < 1) {
            brain_damage += 0.1f;
        }
        if (!health.haveKidney()) {
            brain_damage += 0.002f;
        }
        //更新 自然回复
        if (brain_damage > 0) {
            this.injury(BRAIN_DAMAGE, brain_damage * Utils.DELTA);
        }
    }

    private void handleComa(HealthCapability health) {
        var torso = health.getComponent(TORSO);
        if (torso.abnormal(BodyCondition.HEARTRATE_STOP)) {
            this.injury(BodyCondition.COMA, ConditionAccessor.get(BodyCondition.COMA).maxValue());
        }
    }
}
