
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
import static com.lastimp.dgh.api.enums.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyCondition.PLASTER_CAST;

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
                    WITHDRAW
            ));
        }
        return HEAD_CONDITIONS;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        super.update(health, player);
        this.handleWithdraw(health);
        return this;
    }

    private void handleWithdraw(PlayerHealthCapability health) {
        if (!this.abnormal(WITHDRAW)) return;

        if (this.getConditionValue(WITHDRAW) > health.getComponent(BLOOD).getConditionValue(OPIATE_ADDICTED))
            this.healing(WITHDRAW, -WITHDRAW.healingSpeed * DELTA);
    }

}
