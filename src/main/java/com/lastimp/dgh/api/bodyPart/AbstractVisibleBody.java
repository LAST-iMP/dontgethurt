/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
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
            ANY_BODY_CONDITIONS = List.of(new BodyCondition[]{
                    BURN,
                    INTERNAL_INJURY,
                    OPEN_WOUND,
                    BLEED,
                    INFECTION,
                    FOREIGN_OBJECT,
                    BANDAGED,
                    BANDAGED_DIRTY,
                    OINMENTED
            });
        }
        return ANY_BODY_CONDITIONS;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        handleBandaged(health);
        handleBurning(health);
        handleInternalInjury(health, player);
        handleOpenWound(health);

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
    public int slowDownLevel(PlayerHealthCapability health) {
        return (this.isBandaged() || isBadBandaged()) ? 1 : 0;
    }

    private void handleBandaged(PlayerHealthCapability health) {
        if (isBandaged()) {
            ConditionState bandage = this.getCondition(BANDAGED);
            ConditionState burn = this.getCondition(BURN);
            bandage.setValue(bandage.getValue() - BANDAGED.healingSpeed * DELTA);
            if (BURN.abnormal(burn.getValue())) {
                bandage.setValue(bandage.getValue() - BANDAGED.healingSpeed * DELTA);
            }
            if (!isBandaged()) {
                this.getCondition(BANDAGED_DIRTY).setValue(BANDAGED_DIRTY.maxValue);
            }
        }
        ConditionState burn = this.getCondition(BURN);
        ConditionState openWound = this.getCondition(OPEN_WOUND);
        ConditionState innerInjury = this.getCondition(INTERNAL_INJURY);
        if (this.abnormalWithHidden(BANDAGED_DIRTY) && BURN.abnormal(burn.getHiddenValue())) {
            innerInjury.setValue(Mth.clamp(innerInjury.getValue() + burn.getHiddenValue() * Config.dirty_bandage_ratio * DELTA, INTERNAL_INJURY.minValue, INTERNAL_INJURY.maxValue));
        } else if (this.abnormalWithHidden(BANDAGED_DIRTY) && OPEN_WOUND.abnormal(openWound.getHiddenValue())) {
            innerInjury.setValue(Mth.clamp(innerInjury.getValue() + openWound.getHiddenValue() * Config.dirty_bandage_ratio * DELTA, INTERNAL_INJURY.minValue, INTERNAL_INJURY.maxValue));
        }
    }

    private void handleBurning(PlayerHealthCapability health) {
        if (!this.abnormalWithHidden(BURN)) return;
        this.handleBandageAcc(BURN, Config.bandage_acc);
        this.handleCover(BURN);
        this.handleSelfHealing(BURN, true);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(BURN).getValue() * Config.burn_bleed_ratio;
    }

    private void handleInternalInjury(PlayerHealthCapability health, Player player) {
        if (!this.abnormalWithHidden(INTERNAL_INJURY)) return;
        this.handleSelfHealing(INTERNAL_INJURY, false);

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
        this.handleSelfHealing(OPEN_WOUND, true);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(OPEN_WOUND).getValue() * Config.open_wound_bleed_ratio;
    }

    private void handleBandageAcc(BodyCondition condition, float acc) {
        ConditionState state = this.getCondition(condition);

        if (isBandaged()) {
            state.setHiddenValue(
                    Mth.clamp(state.getHiddenValue() - condition.healingSpeed * DELTA * (isBadBandaged() ? 1.0f : acc),
                            condition.minValue, condition.maxValue)
            );
        }
    }

    protected void handleCover(BodyCondition condition) {
        ConditionState state = this.getCondition(condition);
        if (!isBandaged() && !isBadBandaged()) {
            state.setValue(condition.abnormal(state.getHiddenValue())? state.getHiddenValue() : state.getValue());
            state.setHiddenValue(condition.defaultValue);
        }
    }

    private void handleSelfHealing(BodyCondition condition, boolean hidden) {
        ConditionState state = this.getCondition(condition);
        if (state.getValue() + (hidden ? state.getHiddenValue() : 0) < condition.healingTS + EPS) {
            state.setValue(state.getValue() - condition.healingSpeed * DELTA);
        }
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
