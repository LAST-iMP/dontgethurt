package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyCondition.DRILLED_BONES;
import static com.lastimp.dgh.api.enums.BodyCondition.FRACTURE;

public class BoneImplants extends AbstractPartlyHealItem {
    public BoneImplants(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(DRILLED_BONES)) return false;
            if (!body.abnormal(FRACTURE)) return false;

            body.healing(FRACTURE, -FRACTURE.maxValue);
            return true;
        });
    }
}
