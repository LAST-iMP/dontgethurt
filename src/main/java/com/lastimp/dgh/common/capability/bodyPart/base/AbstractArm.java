package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Consumer;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.BONE_WOOD;

public abstract class AbstractArm extends AbstractExtremities{
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static final Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> uniqueHandlers = new TreeMap<>();
    private static List<ResourceLocation> ARM_CONDITIONS;
    private static Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> ARM_HANDLERS;

    private AttributeInstance attack_speed;

    private UUID uuid_bone_netherite;

    public AbstractArm() {
        super();
    }



    public static void addCondition(ResourceLocation key) {
        uniqueConditions.add(key);
    }

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    public static void addHandler(int priority, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler) {
        uniqueHandlers.put(priority, handler);
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (ARM_CONDITIONS == null) {
            ARM_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            ARM_CONDITIONS.addAll(uniqueConditions);
        }
        return ARM_CONDITIONS;
    }

    @Override
    protected Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> getHandlers() {
        if (ARM_HANDLERS == null) {
            ARM_HANDLERS = new TreeMap<>(super.getHandlers());
            ARM_HANDLERS.putAll(uniqueHandlers);
        }
        return ARM_HANDLERS;
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().addAllowed(ModTags.ORGAN_ARM);
    }

//    @Override
//    protected void updateBoneEffect(LivingEntity entity) {
//        super.updateBoneEffect(entity);
//        if (attack_speed == null) attack_speed = entity.getAttribute(Attributes.ATTACK_SPEED);
//        entity.getAttribute(Attributes.ARMOR);
//
//        updateNetheriteBoneEffect();
//    }
//
//    private void updateNetheriteBoneEffect() {
//        if (uuid_bone_netherite == null)
//            uuid_bone_netherite = UUID.fromString(this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);
//
//        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS) {
//            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) == null)
//                attack_speed.addPermanentModifier(new AttributeModifier(
//                        uuid_bone_netherite,
//                        "arm_bone_netherite",
//                        0.1,
//                        AttributeModifier.Operation.MULTIPLY_BASE
//                ));
//        } else {
//            if (attack_speed != null && attack_speed.getModifier(uuid_bone_netherite) != null)
//                attack_speed.removeModifier(uuid_bone_netherite);
//        }
//    }

    public static float onBreakSpeed(Player player) {
        if (!HealthCapability.has(player)) return 1;
        return HealthCapability.getAndApply(player, health -> {
            var left_arm = health.getComponent(BodyComponents.LEFT_ARM);
            var right_arm = health.getComponent(BodyComponents.RIGHT_ARM);
            int speed_up = 0;
            if (left_arm.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - Utils.EPS)
                speed_up++;
            if (right_arm.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - Utils.EPS)
                speed_up++;
            if (speed_up > 0) {
                return 1.0f + (0.1f * speed_up);
            }
            return 1.0f;
        }, 1.0f);
    }
}
