package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Consumer;

public abstract class AbstractLeg extends AbstractExtremities{
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static final Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> uniqueHandlers = new TreeMap<>();
    private static List<ResourceLocation> LEG_CONDITIONS;
    private static Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> LEG_HANDLERS;

    private AttributeInstance move_speed;
    private AttributeInstance jump_strength;
    private AttributeInstance safe_fall_distance;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

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
        if (LEG_CONDITIONS == null) {
            LEG_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            LEG_CONDITIONS.addAll(uniqueConditions);
        }
        return LEG_CONDITIONS;
    }

    @Override
    protected Map<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>> getHandlers() {
        if (LEG_HANDLERS == null) {
            LEG_HANDLERS = new TreeMap<>(super.getHandlers());
            LEG_HANDLERS.putAll(uniqueHandlers);
        }
        return LEG_HANDLERS;
    }

    @Override
    public int slowDownLevel(HealthCapability health) {
        return super.slowDownLevel(health) + (this.available(health)? 0 : 8);
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().addAllowed(ModTags.ORGAN_LEG);
    }

//    @Override
//    protected void updateBoneEffect(LivingEntity entity) {
//        super.updateBoneEffect(entity);
//        if (move_speed == null) move_speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
//        if (jump_strength == null) jump_strength = entity.getAttribute(Attributes.JUMP_STRENGTH);
//        if (safe_fall_distance == null) safe_fall_distance = entity.getAttribute(Attributes.SAFE_FALL_DISTANCE);
//
//        updateWoodBoneEffect();
//        updateNetheriteBoneEffect();
//    }
//
//    private void updateWoodBoneEffect() {
//        if (uuid_bone_wood == null)
//            uuid_bone_wood = ResourceHelper.ResourceLocation(Utils.MODID, this.getShortID() + "-" + SurgeryBones.ID_WOOD);
//
//        if (this.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - Utils.EPS) {
//            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) == null)
//                move_speed.addPermanentModifier(new AttributeModifier(
//                        uuid_bone_wood,
//                        0.1,
//                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
//                ));
//        } else {
//            if (move_speed != null && move_speed.getModifier(uuid_bone_wood) != null)
//                move_speed.removeModifier(uuid_bone_wood);
//        }
//    }
//
//    private void updateNetheriteBoneEffect() {
//        if (uuid_bone_netherite == null)
//            uuid_bone_netherite = ResourceHelper.ResourceLocation(Utils.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);
//
//        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS) {
//            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) == null)
//                jump_strength.addPermanentModifier(new AttributeModifier(
//                        uuid_bone_netherite,
//                        0.1,
//                        AttributeModifier.Operation.ADD_VALUE
//                ));
//            if (safe_fall_distance != null && safe_fall_distance.getModifier(uuid_bone_netherite) == null)
//                safe_fall_distance.addPermanentModifier(new AttributeModifier(
//                        uuid_bone_netherite,
//                        1,
//                        AttributeModifier.Operation.ADD_VALUE
//                ));
//        } else {
//            if (jump_strength != null && jump_strength.getModifier(uuid_bone_netherite) != null)
//                jump_strength.removeModifier(uuid_bone_netherite);
//            if (safe_fall_distance != null && safe_fall_distance.getModifier(uuid_bone_netherite) != null)
//                safe_fall_distance.removeModifier(uuid_bone_netherite);
//        }
//    }
}
