package com.lastimp.dgh.source.core.bodyPart.base;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.lastimp.dgh.api.bodyPart.ConditionAccessor;

import java.util.UUID;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BONE_NETHERITE;
import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.BONE_WOOD;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
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

        if (this.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - EPS) {
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

        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - EPS) {
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

    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        var entity = event.getEntity();
        if (!HealthCapability.has(entity)) return;

        HealthCapability.getAndApply(entity, health -> {
            var left_leg = health.getComponent(BodyComponents.LEFT_LEG);
            var right_leg = health.getComponent(BodyComponents.RIGHT_LEG);
            int safe_distance = 0;
            if (left_leg.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - EPS)
                safe_distance++;
            if (right_leg.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - EPS)
                safe_distance++;

            float newDist = Math.max(0, event.getDistance() - safe_distance);
            event.setDistance(newDist);
        });
    }
}
