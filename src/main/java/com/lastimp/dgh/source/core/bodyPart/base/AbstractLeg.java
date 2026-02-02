package com.lastimp.dgh.source.core.bodyPart.base;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BONE_WOOD;

public abstract class AbstractLeg extends AbstractExtremities{
    private AttributeInstance move_speed;
    private AttributeInstance jump_strength;
    private AttributeInstance safe_fall_distance;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

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
        if (safe_fall_distance == null) safe_fall_distance = entity.getAttribute(Attributes.SAFE_FALL_DISTANCE);

        updateWoodBoneEffect();
        updateNetheriteBoneEffect();
    }

    private void updateWoodBoneEffect() {
        if (uuid_bone_wood == null)
            uuid_bone_wood = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_WOOD);

        if (this.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - EPS) {
            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) == null)
                move_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_wood,
                        0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        } else {
            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) != null)
                move_speed.removeModifier(uuid_bone_wood);
        }
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - EPS) {
            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) == null)
                jump_strength.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        0.1,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            if (safe_fall_distance != null && safe_fall_distance.getModifier(uuid_bone_netherite) == null)
                safe_fall_distance.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        1,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) != null)
                jump_strength.removeModifier(uuid_bone_netherite);
            if (safe_fall_distance != null && safe_fall_distance.getModifier(uuid_bone_netherite) != null)
                safe_fall_distance.removeModifier(uuid_bone_netherite);
        }
    }
}
