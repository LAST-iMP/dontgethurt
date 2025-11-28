
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.ConditionState;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.BodyCondition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.enums.BodyComponents.HEAD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
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
        this.handleOpiateOverdose();
        this.handleOpiateAddicted(health);
        return this;
    }

    @Override
    public float updateVitalityLost(PlayerHealthCapability health, Player player) {
        float lost = 0;
        if (this.abnormal(BLOOD_LOSS))
            lost += this.getConditionValue(BLOOD_LOSS) * this.getVitalityWeight();
        if (this.abnormal(OPIATE_OVERDOSE))
            lost += Mth.clamp(this.getConditionValue(OPIATE_OVERDOSE) - 0.5f, 0.0f, 0.5f);
        return lost;
    }

    @Override
    public int slowDownLevel(PlayerHealthCapability health) {
        return 0;
    }

    private void handleBloodVolume(PlayerHealthCapability health) {
        if (!this.abnormalWithHidden(BLOOD_LOSS)) return;
        if (this.isBleeding(health)) return;

        ConditionState state = this.getCondition(BLOOD_LOSS);
        if (state.getValue() > BLOOD_LOSS.defaultValue + EPS)
            this.healing(BLOOD_LOSS, - BLOOD_LOSS.healingSpeed * DELTA);
    }

    private boolean isBleeding(PlayerHealthCapability health) {
        for (AbstractBody body : health.visibleParts()) {
            if (BLEED.abnormal(body.getConditionValue(BLEED)))
                return true;
        }
        return false;
    }

    private void handleOpiateOverdose() {
        if (!this.abnormal(OPIATE_OVERDOSE)) return;

        this.healing(OPIATE_OVERDOSE, -OPIATE_OVERDOSE.healingSpeed * DELTA);
    }

    private void handleOpiateAddicted(PlayerHealthCapability health) {
        if (!this.abnormal(OPIATE_ADDICTED)) return;

        this.healing(OPIATE_ADDICTED, -OPIATE_ADDICTED.healingSpeed * DELTA);

        Head head = (Head) health.getComponent(HEAD);
        if (head.getConditionValue(WITHDRAW) < this.getConditionValue(OPIATE_ADDICTED) && !health.getComponent(TORSO).abnormal(ANALGESIA))
            head.healing(WITHDRAW, this.getConditionValue(OPIATE_ADDICTED) * DELTA * Config.withdraw_ratio);
    }
}
