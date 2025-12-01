
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.ConditionState;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class Sutures extends AbstractPartlyHealItem {
    public Sutures(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            AbstractBody body = health.getComponent(component);

            boolean success = false;
            if (success |= body.abnormal(OPEN_WOUND))
                body.healing(OPEN_WOUND, -0.2f);
            if (success |= body.abnormal(SURGERY_INCISION))
                body.setConditionValue(SURGERY_INCISION, SURGERY_INCISION.defaultValue);
            if (success |= body.abnormal(CLAMPED_BLEEDING))
                body.setConditionValue(CLAMPED_BLEEDING, CLAMPED_BLEEDING.defaultValue);
            if (success |= body.abnormal(RETRACTED_SKIN))
                body.setConditionValue(RETRACTED_SKIN, RETRACTED_SKIN.defaultValue);
            if (success |= body.abnormal(DRILLED_BONES))
                body.setConditionValue(DRILLED_BONES, DRILLED_BONES.defaultValue);
            return success;
        });
    }
}
