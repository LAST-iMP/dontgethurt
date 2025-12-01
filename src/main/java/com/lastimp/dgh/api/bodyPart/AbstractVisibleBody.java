package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.BodyCondition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public abstract class AbstractVisibleBody extends AbstractBody {
    private static List<BodyCondition> ANY_BODY_CONDITIONS;
    private float nextTickBleed;

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (ANY_BODY_CONDITIONS == null) {
            ANY_BODY_CONDITIONS = List.of(
                    SURGERY_INCISION,
                    CLAMPED_BLEEDING,
                    RETRACTED_SKIN,
                    DRILLED_BONES,

                    BURN,
                    INTERNAL_INJURY,
                    OPEN_WOUND,
                    BLEED,
                    INFECTION,
                    FOREIGN_OBJECT,
                    BANDAGED,
                    BANDAGED_DIRTY,
                    OINMENTED,

                    FRACTURE,
                    INTENSE_PAIN,
                    PLASTER_CAST,

                    BURN_RES,
                    INTERNAL_RES,
                    OPEN_WOUND_RES
            );
        }
        return ANY_BODY_CONDITIONS;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        handleBandaged(health);
        handleBurning(health);
        handleInternalInjury(health, player);
        handleOpenWound(health);
        handleFracture(health, player);
        handleSurgery(health, player);
        handleBleeding(health);
        return this;
    }

    @Override
    public AbstractBody updatePre(PlayerHealthCapability health, Player player) {
        super.updatePre(health, player);
        this.nextTickBleed = 0;
        return this;
    }

    @Override
    public float updateVitalityLost(PlayerHealthCapability health, Player player) {
        float lost = 0;
        var burn = this.getCondition(BURN);
        var open_wound = this.getCondition(OPEN_WOUND);
        var internal_injury = this.getCondition(INTERNAL_INJURY);
        lost += (burn.getTotalValue() + open_wound.getTotalValue() + internal_injury.getValue()) * this.getVitalityWeight();
        return lost;
    }

    @Override
    public void healing(BodyCondition key, float value) {
        float heal = Mth.clamp(Math.min(-value, this.getConditionValue(key)), 0.0f, 1.0f) * Config.resistance_convert_ratio;
        if (key == BURN) {
            this.addConditionValue(BURN_RES, heal);
        } else if (key == OPEN_WOUND) {
            this.addConditionValue(OPEN_WOUND_RES, heal);
        } else if (key == INTERNAL_INJURY) {
            this.addConditionValue(INTERNAL_RES, heal);
        }
        super.healing(key, value);
    }

    @Override
    public int slowDownLevel(PlayerHealthCapability health) {
        int slowDown = (this.isBandaged() || isBadBandaged()) ? 1 : 0;
        slowDown += this.abnormal(PLASTER_CAST)? 2 : 0;
        return slowDown;
    }

    private void handleBandaged(PlayerHealthCapability health) {
        if (isBandaged()) {
            this.healing(BANDAGED, - BANDAGED.healingSpeed * DELTA);
            if (this.abnormalWithHidden(BURN)) {
                this.addConditionValue(BANDAGED, - BANDAGED.healingSpeed * DELTA);
            }
            if (!isBandaged()) {
                this.getCondition(BANDAGED_DIRTY).setValue(BANDAGED_DIRTY.maxValue);
            }
        }

        if (this.abnormal(BANDAGED_DIRTY) && this.abnormalWithHidden(BURN)) {
            this.injury(BURN, this.getCondition(BURN).getHiddenValue() * Config.dirty_bandage_ratio * DELTA);
        } else if (this.abnormal(BANDAGED_DIRTY) && this.abnormalWithHidden(OPEN_WOUND)) {
            this.injury(INTERNAL_INJURY, this.getCondition(OPEN_WOUND).getHiddenValue() * Config.dirty_bandage_ratio * DELTA);
        }
    }

    private void handleBurning(PlayerHealthCapability health) {
        if (!this.abnormalWithHidden(BURN)) return;
        this.handleBandageAcc(BURN, Config.bandage_acc);
        this.handleCover(BURN);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(BURN).getValue() * Config.burn_bleed_ratio;
    }

    private void handleInternalInjury(PlayerHealthCapability health, Player player) {
        if (!this.abnormalWithHidden(INTERNAL_INJURY)) return;

        this.nextTickBleed += this.getCondition(INTERNAL_INJURY).getValue() * Config.internal_bleed_ratio;

        float saturation = player.getFoodData().getSaturationLevel();
        float delta = INTERNAL_INJURY.healingSpeed * DELTA;
        if (saturation > 0) {
            if (INTERNAL_INJURY.abnormal(this.getCondition(INTERNAL_INJURY).getHiddenValue()))
                this.healingHidden(INTERNAL_INJURY, -delta);
            else
                this.healing(INTERNAL_INJURY, -delta * Config.internal_food_healing);
            player.causeFoodExhaustion(delta * Config.internal_food_healing * 2);
        }
    }

    private void handleOpenWound(PlayerHealthCapability health) {
        if (!this.abnormalWithHidden(OPEN_WOUND)) return;
        this.handleBandageAcc(OPEN_WOUND, Config.bandage_acc);
        this.handleCover(OPEN_WOUND);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(OPEN_WOUND).getValue() * Config.open_wound_bleed_ratio;
    }

    private void handleBandageAcc(BodyCondition condition, float acc) {
        if (isBandaged()) {
            this.healingHidden(condition, - condition.healingSpeed * DELTA * (isBadBandaged() ? 1.0f : acc));
        }
    }

    protected void handleCover(BodyCondition condition) {
        ConditionState state = this.getCondition(condition);
        if (!isBandaged() && !isBadBandaged()) {
            this.setConditionValue(condition, state.getValue() + state.getHiddenValue());
            state.setHiddenValue(condition.defaultValue);
        }
    }

    private void handleFracture(PlayerHealthCapability health, Player player) {
        if (!this.abnormalWithHidden(FRACTURE)) return;
        this.handleCover(FRACTURE);

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, INTENSE_PAIN.maxValue);
        }

        if (this.abnormal(PLASTER_CAST))
            this.healingHidden(FRACTURE, -FRACTURE.healingSpeed * DELTA);
    }

    private void handleSurgery(PlayerHealthCapability health, Player player) {
        Torso torso = (Torso) health.getComponent(TORSO);
        Head head = (Head) health.getComponent(HEAD);
        if (this.abnormal(SURGERY_INCISION)) {
            if (!this.abnormal(CLAMPED_BLEEDING))
                this.nextTickBleed += 0.23f;
            if (!torso.abnormal(ANALGESIA))
                head.injury(TRAUMATIC_SHOCK, 0.02f * DELTA);
        }
        if (this.abnormal(RETRACTED_SKIN))
            if (!torso.abnormal(ANALGESIA))
                head.injury(TRAUMATIC_SHOCK, 0.02f * DELTA);
        if (this.abnormal(DRILLED_BONES))
            if (!torso.abnormal(ANALGESIA))
                head.injury(TRAUMATIC_SHOCK, 0.015f * DELTA);
    }

    public boolean isBandaged() {
        return BANDAGED.abnormal(this.getConditionValue(BANDAGED));
    }

    public boolean isBadBandaged() {
        return BANDAGED_DIRTY.abnormal(this.getConditionValue(BANDAGED_DIRTY));
    }

    private void handleBleeding(PlayerHealthCapability health) {
        this.getCondition(BLEED).setValue(this.nextTickBleed);

        PlayerBlood blood = (PlayerBlood) health.getComponent(BLOOD);
        blood.addConditionValue(BLOOD_LOSS, this.nextTickBleed * DELTA * Config.bleed_volume_ratio);
    }
}
