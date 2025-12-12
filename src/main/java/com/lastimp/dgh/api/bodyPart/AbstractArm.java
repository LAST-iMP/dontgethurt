package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BONE_WOOD;

public abstract class AbstractArm extends AbstractExtremities{
    private AttributeInstance break_speed ;
    private AttributeInstance attack_speed;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

    public AbstractArm() {
        super();
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        super.update(health, player);
        return this;
    }

    protected void updateBoneEffect(Player player) {
        super.updateBoneEffect(player);
        if (break_speed == null) break_speed = player.getAttribute(Attributes.BLOCK_BREAK_SPEED);
        if (attack_speed == null) attack_speed = player.getAttribute(Attributes.ATTACK_SPEED);
        player.getAttribute(Attributes.ARMOR);

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
