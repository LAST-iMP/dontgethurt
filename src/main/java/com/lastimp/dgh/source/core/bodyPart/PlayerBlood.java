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

package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.ConditionState;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.BodyCondition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedList;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class PlayerBlood extends AbstractBody {
    private static List<BodyCondition> BLOOD_CONDITIONS;

    public PlayerBlood() {
        super();
    }

    public PlayerBlood(Void v) {
        this();
    }

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (BLOOD_CONDITIONS == null) {
            BLOOD_CONDITIONS = List.of(new BodyCondition[]{
                    SEPSIS,
                    HEMOTRANSFUSION,
                    BLOOD_LOSS,
                    BLOOD_PRESSURE,
                    PH_LEVEL,
                    IMMUNITY,

                    OPIATE_OVERDOSE,
                    OPIATE_ADDICTED
            });
        }
        return BLOOD_CONDITIONS;
    }

    @Override
    public float getVitalityWeight() {
        return 1;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        this.handleBloodVolume(health);
        return this;
    }

    @Override
    public float updateVitalityLost(PlayerHealthCapability health, Player player) {
        float lost = 0;
        var blood_loss = this.getCondition(BLOOD_LOSS);
        if (BLOOD_LOSS.abnormal(blood_loss.getValue()))
            lost += blood_loss.getValue();
        return lost;
    }

    private void handleBloodVolume(PlayerHealthCapability health) {
        if (!this.abnormalWithHidden(BLOOD_LOSS)) return;
        if (this.isBleeding(health)) return;

        ConditionState state = this.getCondition(BLOOD_LOSS);
        if (state.getValue() > BLOOD_LOSS.defaultValue + EPS)
            state.setValue(Mth.clamp(state.getValue() - BLOOD_LOSS.healingSpeed * DELTA, BLOOD_LOSS.minValue, BLOOD_LOSS.maxValue));
    }

    private boolean isBleeding(PlayerHealthCapability health) {
        for (AbstractBody body : health.visibleParts()) {
            if (BLEED.abnormal(body.getConditionValue(BLEED)))
                return true;
        }
        return false;
    }
}
