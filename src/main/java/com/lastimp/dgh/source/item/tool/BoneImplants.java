package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.DRILLED_BONES;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.FRACTURE;

public class BoneImplants extends AbstractPartlyHealItem {
    private final ResourceLocation boneType;

    public BoneImplants(Properties properties) {
        super(properties);
        this.boneType = null;
    }

    public BoneImplants(Properties properties, ResourceLocation boneType) {
        super(properties);
        this.boneType = boneType;
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (body.abnormal(DRILLED_BONES) && body.abnormal(FRACTURE)) {
                if (body.boneCrafted() != boneType) return false;
                body.healing(FRACTURE, -BodyCondition.get(FRACTURE).maxValue());
                return true;
            }
            return false;
        });
    }
}
