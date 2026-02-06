package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.item.tool.SurgeryBones;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;

import java.util.UUID;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.BONE_WOOD;

public abstract class AbstractLeg extends AbstractExtremities{
    private AttributeInstance move_speed;
    private AttributeInstance jump_strength;

    private UUID uuid_bone_wood;
    private UUID uuid_bone_netherite;

    @Override
    public int slowDownLevel(HealthCapability health) {
        return super.slowDownLevel(health) + (this.available(health)? 0 : 8);
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().addAllowed(ModTags.ORGAN_LEG);
    }

    @Override
    protected void updateBoneEffect(LivingEntity entity) {
        super.updateBoneEffect(entity);
        if (move_speed == null) move_speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (jump_strength == null) jump_strength = entity.getAttribute(Attributes.JUMP_STRENGTH);

        updateWoodBoneEffect();
        updateNetheriteBoneEffect();
    }

    private void updateWoodBoneEffect() {
        if (uuid_bone_wood == null)
            uuid_bone_wood = UUID.fromString(this.getShortID() + "-" + SurgeryBones.ID_WOOD);

        if (this.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - Utils.EPS) {
            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) == null)
                move_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_wood,
                        "arm_bone_wood",
                        0.1,
                        AttributeModifier.Operation.MULTIPLY_BASE
                ));
        } else {
            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) != null)
                move_speed.removeModifier(uuid_bone_wood);
        }
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = UUID.fromString(this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS) {
            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) == null)
                jump_strength.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        "arm_bone_netherite",
                        0.1,
                        AttributeModifier.Operation.ADDITION
                ));
        } else {
            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) != null)
                jump_strength.removeModifier(uuid_bone_netherite);
        }
    }

    public static int onFall(LivingEntity livingEntity) {
        if (!HealthCapability.has(livingEntity)) return 0;

        return HealthCapability.getAndApply(livingEntity, health -> {
            var left_leg = health.getComponent(BodyComponents.LEFT_LEG);
            var right_leg = health.getComponent(BodyComponents.RIGHT_LEG);
            int safe_distance = 0;
            if (left_leg.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS)
                safe_distance++;
            if (right_leg.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS)
                safe_distance++;
            return safe_distance;
        }, 0);
    }
}
