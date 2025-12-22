
package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public abstract class AbstractVisibleBody extends AbstractBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> ANY_BODY_CONDITIONS;
    private float nextTickBleed;

    private AttributeInstance armor;
    private AttributeInstance armor_toughness;
    private AttributeInstance knock_back_resist;

    private ResourceLocation uuid_bone_stone;
    private ResourceLocation uuid_bone_copper;
    private ResourceLocation uuid_bone_iron;
    private ResourceLocation uuid_bone_gold;
    private ResourceLocation uuid_bone_dimond;
    private ResourceLocation uuid_bone_netherite;

    public static void addCondition(Collection<ResourceLocation> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public List<ResourceLocation> getBodyConditions() {
        if (ANY_BODY_CONDITIONS == null) {
            ANY_BODY_CONDITIONS = new ArrayList<>(uniqueConditions);
        }
        return ANY_BODY_CONDITIONS;
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        handleBandaged();
        handleBurning(entity);
        handleInternalInjury(entity);
        handleOpenWound(entity);
        handleFracture(health);
        handleSurgery(health);
        handleBleeding(health);
        updateBoneEffect(entity);
        return this;
    }

    @Override
    public AbstractBody updatePre(HealthCapability health, LivingEntity entity) {
        super.updatePre(health, entity);
        this.nextTickBleed = 0;
        return this;
    }

    @Override
    public float updateVitalityLost(HealthCapability health, LivingEntity entity) {
        float lost = 0;
        var burn = this.getCondition(BURN);
        var open_wound = this.getCondition(OPEN_WOUND);
        var internal_injury = this.getCondition(INTERNAL_INJURY);
        lost += (burn.getTotalValue() + open_wound.getTotalValue() + internal_injury.getValue()) * this.getVitalityWeight();
        return lost;
    }

    @Override
    public void healing(ResourceLocation key, float value) {
        float heal = Mth.clamp(Math.min(-value, this.getConditionValue(key)), 0.0f, 2.0f) * Config.resistance_convert_ratio;
        handleResist(key, heal);
        super.healing(key, value);
    }

    @Override
    public void healingHidden(ResourceLocation key, float value) {
        float heal = Mth.clamp(Math.min(-value, this.getConditionHidden(key)), 0.0f, 2.0f) * Config.resistance_convert_ratio;
        handleResist(key, heal);
        super.healingHidden(key, value);
    }

    private void handleResist(ResourceLocation key, float heal) {
        if (key == BURN) {
            this.addConditionValue(BURN_RES, heal);
        } else if (key == OPEN_WOUND) {
            this.addConditionValue(OPEN_WOUND_RES, heal);
        } else if (key == INTERNAL_INJURY) {
            this.addConditionValue(INTERNAL_RES, heal);
        }
    }

    public int slowDownLevel(HealthCapability health) {
        int slowDown = (this.isBandaged() || isBadBandaged()) ? 1 : 0;
        slowDown += this.abnormal(PLASTER_CAST)? 2 : 0;
        return slowDown;
    }

    private void handleBandaged() {
        if (isBandaged()) {
            var bandage = BodyCondition.get(BANDAGED);
            this.healing(BANDAGED, - bandage.healingSpeed() * DELTA);
            if (this.abnormalWithHidden(BURN)) {
                this.addConditionValue(BANDAGED, - bandage.healingSpeed() * DELTA);
            }
            if (!isBandaged()) {
                var bandageDirty = BodyCondition.get(BANDAGED_DIRTY);
                this.getCondition(BANDAGED_DIRTY).setValue(bandageDirty.maxValue());
            }
        }

        if (this.abnormal(BANDAGED_DIRTY) && this.abnormalWithHidden(BURN)) {
            this.injury(BURN, this.getCondition(BURN).getHiddenValue() * Config.dirty_bandage_ratio * DELTA);
        } else if (this.abnormal(BANDAGED_DIRTY) && this.abnormalWithHidden(OPEN_WOUND)) {
            this.injury(INTERNAL_INJURY, this.getCondition(OPEN_WOUND).getHiddenValue() * Config.dirty_bandage_ratio * DELTA);
        }
    }

    private void handleBurning(LivingEntity entity) {
        if (!this.abnormalWithHidden(BURN)) return;
        this.handleBandageAcc(BURN, Config.bandage_acc);
        this.handleCover(BURN);
        this.handleFoodAcc(entity, BURN, 1.0f);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(BURN).getValue() * Config.burn_bleed_ratio;
    }

    private void handleInternalInjury(LivingEntity entity) {
        if (!this.abnormalWithHidden(INTERNAL_INJURY)) return;
        this.handleFoodAcc(entity, INTERNAL_INJURY, 1.0f);

        this.nextTickBleed += this.getCondition(INTERNAL_INJURY).getValue() * Config.internal_bleed_ratio;
    }

    private void handleOpenWound(LivingEntity entity) {
        if (!this.abnormalWithHidden(OPEN_WOUND)) return;
        this.handleBandageAcc(OPEN_WOUND, Config.bandage_acc);
        this.handleCover(OPEN_WOUND);
        this.handleFoodAcc(entity, OPEN_WOUND, 1.0f);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(OPEN_WOUND).getValue() * Config.open_wound_bleed_ratio;
    }

    private void handleBandageAcc(ResourceLocation condition, float acc) {
        if (isBandaged()) {
            this.healingHidden(condition, - BodyCondition.get(condition).healingSpeed() * DELTA * (isBadBandaged() ? 1.0f : acc));
        }
    }

    protected void handleCover(ResourceLocation condition) {
        ConditionState state = this.getCondition(condition);
        if (!isBandaged() && !isBadBandaged()) {
            this.setConditionValue(condition, state.getValue() + state.getHiddenValue());
            state.setHiddenValue(BodyCondition.get(condition).defaultValue());
        }
    }

    protected void handleFoodAcc(LivingEntity entity, ResourceLocation condition, float acc) {
        if (!this.abnormalWithHidden(condition)) return;
        if (!(entity instanceof ServerPlayer player)) return;

        var food = player.getFoodData();
        if (food.getFoodLevel() < 19) return;
        var state = this.getCondition(condition);
        if (BodyCondition.get(condition).healingTS() < state.getTotalValue()) return;

        if (this.abnormalOnlyHidden(condition)) {
            this.healingHidden(condition, - BodyCondition.get(condition).healingSpeed() * DELTA * acc);
        } else {
            this.healing(condition, - BodyCondition.get(condition).healingSpeed() * DELTA * acc);
        }
    }

    private void handleFracture(HealthCapability health) {
        if (!this.abnormalWithHidden(FRACTURE)) return;
        this.handleCover(FRACTURE);

        Torso torso = (Torso) health.getComponent(TORSO);
        if (!torso.abnormal(ANALGESIA) && !this.isBandaged() && !this.isBadBandaged()) {
            this.setConditionValue(INTENSE_PAIN, BodyCondition.get(INTENSE_PAIN).maxValue());
        }

        if (this.abnormal(PLASTER_CAST) && this.boneCrafted() == null)
            this.healingHidden(FRACTURE, -BodyCondition.get(FRACTURE).healingSpeed() * DELTA);
    }

    private void handleSurgery(HealthCapability health) {
        Head head = (Head) health.getComponent(HEAD);
        float factor = health.safeSurgery() ? 0.1f : 1;
        if (this.abnormal(SURGERY_INCISION)) {
            if (!this.abnormal(CLAMPED_BLEEDING)) {
                Blood blood = (Blood) health.getComponent(BLOOD);
                blood.addConditionValue(BLOOD_LOSS, 0.007f * DELTA);
            }
            head.injury(TRAUMATIC_SHOCK, 0.02f * DELTA * factor);
        }
        if (this.abnormal(RETRACTED_SKIN))
            head.injury(TRAUMATIC_SHOCK, 0.02f * DELTA * factor);
        if (this.abnormal(DRILLED_BONES))
            head.injury(TRAUMATIC_SHOCK, 0.015f * DELTA * factor);
        if (this.abnormal(SAWED_BONES))
            head.injury(TRAUMATIC_SHOCK, 0.015f * DELTA * factor);
    }

    private void handleBleeding(HealthCapability health) {
        if (this.abnormal(CLAMPED_BLEEDING))
            this.nextTickBleed = 0;
        this.getCondition(BLEED).setValue(this.nextTickBleed);

        Blood blood = (Blood) health.getComponent(BLOOD);
        blood.addConditionValue(BLOOD_LOSS, this.nextTickBleed * DELTA * Config.bleed_volume_ratio);
    }

    protected void updateBoneEffect(LivingEntity entity) {
        if (armor == null) armor = entity.getAttribute(Attributes.ARMOR);
        if (armor_toughness == null) armor_toughness = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (knock_back_resist == null) knock_back_resist = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        updateStoneBoneEffect();
        updateCopperBoneEffect();
        updateIronBoneEffect();
        updateGoldBoneEffect();
        updateDimondBoneEffect();
        updateNetheriteBoneEffect();
    }

    private void updateStoneBoneEffect() {
        if (uuid_bone_stone == null)
            uuid_bone_stone = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_STONE);

        if (this.getConditionHidden(BONE_STONE) > BodyCondition.get(BONE_STONE).maxValue() - EPS) {
            if (knock_back_resist != null && knock_back_resist.getModifier(uuid_bone_stone) == null)
                knock_back_resist.addPermanentModifier(new AttributeModifier(
                        uuid_bone_stone,
                        0.15,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            if (armor != null && armor.getModifier(uuid_bone_stone) == null)
                armor.addPermanentModifier(new AttributeModifier(
                        uuid_bone_stone,
                        0.5,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (knock_back_resist != null && knock_back_resist.getModifier(uuid_bone_stone) != null)
                knock_back_resist.removeModifier(uuid_bone_stone);
            if (armor != null && armor.getModifier(uuid_bone_stone) != null)
                armor.removeModifier(uuid_bone_stone);
        }
    }

    private void updateCopperBoneEffect() {
        if (uuid_bone_copper == null)
            uuid_bone_copper = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_COPPER);

        if (this.getConditionHidden(BONE_COPPER) > BodyCondition.get(BONE_COPPER).maxValue() - EPS) {
            if (armor != null && armor.getModifier(uuid_bone_copper) == null)
                armor.addPermanentModifier(new AttributeModifier(
                        uuid_bone_copper,
                        0.5,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_copper) == null)
                armor_toughness.addPermanentModifier(new AttributeModifier(
                        uuid_bone_copper,
                        0.5,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (armor != null && armor.getModifier(uuid_bone_copper) != null)
                armor.removeModifier(uuid_bone_copper);
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_copper) != null)
                armor_toughness.removeModifier(uuid_bone_copper);
        }
    }

    private void updateIronBoneEffect() {
        if (uuid_bone_iron == null)
            uuid_bone_iron = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_IRON);

        if (this.getConditionHidden(BONE_IRON) > BodyCondition.get(BONE_IRON).maxValue() - EPS) {
            if (armor != null && armor.getModifier(uuid_bone_iron) == null)
                armor.addPermanentModifier(new AttributeModifier(
                        uuid_bone_iron,
                        1,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (armor != null && armor.getModifier(uuid_bone_iron) != null)
                armor.removeModifier(uuid_bone_iron);
        }
    }

    private void updateGoldBoneEffect() {
        if (uuid_bone_gold == null)
            uuid_bone_gold = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_GOLD);

        if (this.getConditionHidden(BONE_GOLD) > BodyCondition.get(BONE_GOLD).maxValue() - EPS) {
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_gold) == null)
                armor_toughness.addPermanentModifier(new AttributeModifier(
                        uuid_bone_gold,
                        2,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_gold) != null)
                armor_toughness.removeModifier(uuid_bone_gold);
        }
    }

    private void updateDimondBoneEffect() {
        if (uuid_bone_dimond == null)
            uuid_bone_dimond = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_DIMOND);

        if (this.getConditionHidden(BONE_DIMOND) > BodyCondition.get(BONE_DIMOND).maxValue() - EPS) {
            if (armor != null && armor.getModifier(uuid_bone_dimond) == null)
                armor.addPermanentModifier(new AttributeModifier(
                        uuid_bone_dimond,
                        2,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (armor != null && armor.getModifier(uuid_bone_dimond) != null)
                armor.removeModifier(uuid_bone_dimond);
        }
    }

    private void updateNetheriteBoneEffect() {
        if (uuid_bone_netherite == null)
            uuid_bone_netherite = Common.ResourceLocation(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > BodyCondition.get(BONE_NETHERITE).maxValue() - EPS) {
            if (armor != null && armor.getModifier(uuid_bone_netherite) == null)
                armor.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        2,
                        AttributeModifier.Operation.ADD_VALUE
                ));
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_netherite) == null)
                armor_toughness.addPermanentModifier(new AttributeModifier(
                        uuid_bone_netherite,
                        1,
                        AttributeModifier.Operation.ADD_VALUE
                ));
        } else {
            if (armor != null && armor.getModifier(uuid_bone_netherite) != null)
                armor.removeModifier(uuid_bone_netherite);
            if (armor_toughness != null && armor_toughness.getModifier(uuid_bone_netherite) != null)
                armor_toughness.removeModifier(uuid_bone_netherite);
        }
    }

    public boolean isBandaged() {
        return this.abnormal(BANDAGED);
    }

    public boolean isBadBandaged() {
        return this.abnormal(BANDAGED_DIRTY);
    }

    public float fractThreshold () {
        float value = Config.baseFractureThreshold;
        var bone = this.boneCrafted();
        if (bone == BONE_COPPER) {
            value += 0.1f;
        } else if (bone == BONE_GOLD) {
            value += 0.2f;
        } else if (bone == BONE_IRON) {
            value += 0.05f;
        } else if (bone == BONE_NETHERITE) {
            value += 0.1f;
        }
        return value;
    }

    public int fractCheckTimes () {
        var bone = this.boneCrafted();
        if (bone == BONE_WOOD) return -1;
        if (bone == BONE_DIMOND) return 1;
        if (bone == BONE_NETHERITE) return 1;
        return 0;
    }

    public ResourceLocation boneCrafted() {
        for (var key : BodyCondition.bones.keySet()) {
            if (this.abnormalWithHidden(key))
                return key;
        }
        return null;
    }
}
