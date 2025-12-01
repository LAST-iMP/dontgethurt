
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.BLOOD;
import static com.lastimp.dgh.api.enums.BodyComponents.TORSO;
import static com.lastimp.dgh.api.enums.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyCondition.BRAIN_DAMAGE;

public class Head extends AbstractVisibleBody {
    private static List<BodyCondition> HEAD_CONDITIONS;

    public Head() {
        super();
    }

    public Head(Void v) {
        this();
    }

    @Override
    public float getVitalityWeight() {
        return 1f;
    }

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (HEAD_CONDITIONS == null) {
            HEAD_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            HEAD_CONDITIONS.addAll(List.of(
                    WITHDRAW,
                    TRAUMATIC_SHOCK,
                    BRAIN_DAMAGE
            ));
        }
        return HEAD_CONDITIONS;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        super.update(health, player);
        this.handleWithdraw(health);
        this.handleTraumaticShock(health);
        return this;
    }

    @Override
    public int slowDownLevel(PlayerHealthCapability health) {
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
    public float updateVitalityLost(PlayerHealthCapability health, Player player) {
        var loss = super.updateVitalityLost(health, player);
        loss += this.getVitalityWeight() * this.getConditionValue(BRAIN_DAMAGE);
        return loss;
    }

    private void handleWithdraw(PlayerHealthCapability health) {
        if (!this.abnormal(WITHDRAW)) return;

        if (this.getConditionValue(WITHDRAW) > health.getComponent(BLOOD).getConditionValue(OPIATE_ADDICTED))
            this.healing(WITHDRAW, -WITHDRAW.healingSpeed * DELTA);
    }

    private void handleTraumaticShock(PlayerHealthCapability health) {
        if (!this.abnormal(TRAUMATIC_SHOCK)) return;

        var value = this.getConditionValue(TRAUMATIC_SHOCK);
        if (value > 0.3f)
            health.getComponent(TORSO).injury(RESPIRATORY_ARREST, RESPIRATORY_ARREST.maxValue);
        if (value > 0.1f)
            this.injury(BRAIN_DAMAGE, value * 0.01f * DELTA);
    }
}
