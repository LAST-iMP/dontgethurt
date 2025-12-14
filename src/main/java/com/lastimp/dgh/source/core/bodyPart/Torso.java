
package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class Torso extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> TORSO_CONDITIONS;

    private AttributeInstance fly_speed;
    private AttributeInstance knock_back_resist;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

    public Torso() {
        super();
    }

    public Torso (Void v) {
        this();
    }

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (TORSO_CONDITIONS == null) {
            TORSO_CONDITIONS = new ArrayList<>(super.getBodyConditions());
            TORSO_CONDITIONS.addAll(uniqueConditions);
        }
        return TORSO_CONDITIONS;
    }

    @Override
    public float getVitalityWeight() {
        return 0.8f;
    }

    @Override
    public String getShortID() {
        return "torso";
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleRespiratoryArrest();
        return this;
    }

    @Override
    protected void updateBoneEffect(LivingEntity entity) {
        super.updateBoneEffect(entity);
        if (fly_speed == null) fly_speed = entity.getAttribute(Attributes.FLYING_SPEED);
        if (knock_back_resist == null) knock_back_resist = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        updateWoodBoneEffect();
        updateNetheriteBoneEffect();
    }

    private void updateWoodBoneEffect() {
        if (uuid_bone_wood == null)
            uuid_bone_wood = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_WOOD);

        if (this.getConditionHidden(BONE_WOOD) > BodyCondition.get(BONE_WOOD).maxValue() - EPS) {
            if (fly_speed != null && fly_speed.getModifier(uuid_bone_wood) == null)
                fly_speed.addPermanentModifier(new AttributeModifier(
                        uuid_bone_wood,
                        0.2,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        } else {
            if (fly_speed != null && fly_speed.getModifier(uuid_bone_wood) != null)
                fly_speed.removeModifier(uuid_bone_wood);
        }
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > BodyCondition.get(BONE_NETHERITE).maxValue() - EPS) {
            if (knock_back_resist != null && knock_back_resist.getModifier(uuid_bone_netherite) == null)
                knock_back_resist.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        0.25,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                ));
        } else {
            if (knock_back_resist != null && knock_back_resist.getModifier(uuid_bone_netherite) != null)
                knock_back_resist.removeModifier(uuid_bone_netherite);
        }
    }

    @Override
    public float fractThreshold () {
        return Config.baseFractureThreshold + 0.1f;
    }

    private void handleRespiratoryArrest() {
        if (!this.abnormalOnlyHidden(SAWED_BONES)) return;

        this.injury(RESPIRATORY_ARREST, BodyCondition.get(RESPIRATORY_ARREST).maxValue());
    }

    public boolean safeSurgery() {
        return this.abnormal(ANALGESIA);
    }

}
