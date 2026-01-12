package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

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
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (body instanceof AbstractExtremities extremities) {
                if (extremities.abnormal(TRAUMATIC_AMPUTATION) || extremities.abnormal(SURGICAL_AMPUTATION)) return false;
            }

            if (body.abnormal(DRILLED_BONES) && body.abnormal(FRACTURE)) {
                if (body.boneCrafted() != boneType) return false;
                body.healing(FRACTURE, -BodyCondition.get(FRACTURE).maxValue());
                body.healing(BONE_DAMAGE, -BodyCondition.get(BONE_DAMAGE).maxValue());
                body.healing(BONE_DEATH, -BodyCondition.get(BONE_DEATH).maxValue());
                return true;
            }
            return false;
        }, false);
    }
}
