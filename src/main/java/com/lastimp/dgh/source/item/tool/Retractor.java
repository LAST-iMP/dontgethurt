package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.CLAMPED_BLEEDING;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.RETRACTED_SKIN;

public class Retractor extends AbstractPartlyHealItem {
    public Retractor(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(CLAMPED_BLEEDING)) return false;
            if (body.abnormal(RETRACTED_SKIN)) return false;

            body.setConditionValue(RETRACTED_SKIN, BodyCondition.get(RETRACTED_SKIN).maxValue());
            return true;
        });
    }
}
