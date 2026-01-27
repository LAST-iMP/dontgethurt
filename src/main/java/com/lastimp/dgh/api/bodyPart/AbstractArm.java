package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.DontGetHurt;
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
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_WOOD;

public abstract class AbstractArm extends AbstractExtremities{
    private AttributeInstance break_speed ;
    private AttributeInstance attack_speed;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        return this;
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().setValidator((slot, stack) -> {
            if (stack.is(ModTags.ORGAN_ARM)) return true;
            return false;
        });
    }

    @Override
    protected void updateBoneEffect(LivingEntity entity) {
        super.updateBoneEffect(entity);
        if (break_speed == null) break_speed = entity.getAttribute(Attributes.BLOCK_BREAK_SPEED);
        if (attack_speed == null) attack_speed = entity.getAttribute(Attributes.ATTACK_SPEED);
        entity.getAttribute(Attributes.ARMOR);

        updateWoodBoneEffect();
        updateNetheriteBoneEffect();
    }

    private void updateWoodBoneEffect() {
        if (uuid_bone_wood == null)
            uuid_bone_wood = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_WOOD);

        if (this.getConditionHidden(BONE_WOOD) > BodyCondition.get(BONE_WOOD).maxValue() - EPS) {
            if (break_speed != null && break_speed.getModifier(uuid_bone_wood) == null)
                break_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_wood,
                        0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        } else {
            if (break_speed != null && break_speed.getModifier(uuid_bone_wood) != null)
                break_speed.removeModifier(uuid_bone_wood);
        }
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > BodyCondition.get(BONE_NETHERITE).maxValue() - EPS) {
            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) == null)
                attack_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        } else {
            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) != null)
                attack_speed.removeModifier(uuid_bone_netherite);
        }
    }
}
