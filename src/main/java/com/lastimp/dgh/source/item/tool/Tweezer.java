package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class Tweezer extends AbstractPartlyHealItem {
    public Tweezer(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);

            body.injury(OPEN_WOUND, 0.05f);
            if (body.abnormal(RETRACTED_SKIN)) {
                body.healing(INTERNAL_INJURY, -Mth.randomBetween(Utils.randomSource, 0.03f, 0.1f));
            } else {
                body.setConditionValue(INTENSE_PAIN, INTENSE_PAIN.maxValue);
            }
            return true;
        });
    }
}
