
package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.config.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.tool.SurgeryBones;
import com.lastimp.dgh.source.register.ModEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.DontGetHurt.EPS;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public abstract class AbstractVisibleBody extends AbstractBody {
    private static final Collection<Identifier> uniqueConditions = new LinkedHashSet<>();
    private static List<Identifier> ANY_BODY_CONDITIONS;
    private float nextTickBleed;

    private AttributeInstance armor;
    private AttributeInstance armor_toughness;
    private AttributeInstance knock_back_resist;

    private Identifier uuid_bone_stone;
    private Identifier uuid_bone_copper;
    private Identifier uuid_bone_iron;
    private Identifier uuid_bone_gold;
    private Identifier uuid_bone_dimond;
    private Identifier uuid_bone_netherite;

    private int nextFractureTick = 1200;

    public static void addCondition(Collection<Identifier> key) {
        uniqueConditions.addAll(key);
    }

    @Override
    public List<Identifier> getBodyConditions() {
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
        handlePassThrough(entity);
        handleInfection();
        handleFracture(health);
        handleSurgery(health);
        handleBleeding(health);
        handleArterialBleeding(health);
        updateBoneEffect(entity);
        handleBoneDamage(health);
        handleBoneDeath();
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
        var pass_through = this.getCondition(PASS_THROUGH);
        var internal_injury = this.getCondition(INTERNAL_INJURY);
        lost += (burn.getTotalValue() + open_wound.getTotalValue() + internal_injury.getValue() + pass_through.getValue()) * this.getVitalityWeight();
        return lost;
    }

    @Override
    public void healing(Identifier key, float value) {
        float heal = Mth.clamp(Math.min(-value, this.getConditionValue(key)), 0.0f, 2.0f) * Config.resistance_convert_ratio;
        handleResist(key, heal);
        super.healing(key, value);
    }

    @Override
    public void healingHidden(Identifier key, float value) {
        float heal = Mth.clamp(Math.min(-value, this.getConditionHidden(key)), 0.0f, 2.0f) * Config.resistance_convert_ratio;
        handleResist(key, heal);
        super.healingHidden(key, value);
    }

    private void handleResist(Identifier key, float heal) {
        if (key == BURN) {
            this.addConditionValue(BURN_RES, heal);
        } else if (key == OPEN_WOUND || key == PASS_THROUGH) {
            this.addConditionValue(OPEN_WOUND_RES, heal);
        } else if (key == INTERNAL_INJURY) {
            this.addConditionValue(INTERNAL_RES, heal);
        }
    }

    public int slowDownLevel(HealthCapability health) {
        int slowDown = (this.isBandaged() || isBadBandaged()) ? 1 : 0;
        slowDown += this.abnormal(PLASTER_CAST)? 2 : 0;
        if (this.abnormal(SURGERY_INCISION)) slowDown += 20;
        return slowDown;
    }

    private void handleBandaged() {
        if (isBandaged()) {
            var bandage = BodyCondition.get(BANDAGED);
            var factor = 0.25f;
            factor = this.abnormalWithHidden(OPEN_WOUND) || this.abnormalWithHidden(PASS_THROUGH) ? 1 : factor;
            factor = this.abnormalWithHidden(BURN) ? 2 : factor;
            this.addConditionValue(BANDAGED, - bandage.healingSpeed() * DELTA * factor);
            if (!isBandaged()) {
                this.injury(BANDAGED_DIRTY, BodyCondition.get(BANDAGED_DIRTY).maxValue());
            }
        }
    }

    private void handleBurning(LivingEntity entity) {
        if (!this.abnormalWithHidden(BURN)) return;
        this.handleBandageAcc(BURN, Config.bandage_acc);
        this.handleCover(BURN);
        this.handleFoodAcc(entity, BURN, 1.0f);
        this.handleInjuryInfection(BURN);
        this.handleCombatStimulant(entity, BURN);

        if (!isBandaged()) this.nextTickBleed += this.getCondition(BURN).getValue() * Config.burn_bleed_ratio;
    }

    private void handleInternalInjury(LivingEntity entity) {
        if (!this.abnormalWithHidden(INTERNAL_INJURY)) return;
        this.handleFoodAcc(entity, INTERNAL_INJURY, 1.0f);
        this.handleCombatStimulant(entity, INTERNAL_INJURY);

        this.nextTickBleed += this.getCondition(INTERNAL_INJURY).getValue() * Config.internal_bleed_ratio;
    }

    private void handleOpenWound(LivingEntity entity) {
        if (!this.abnormalWithHidden(OPEN_WOUND)) return;
        this.handleBandageAcc(OPEN_WOUND, Config.bandage_acc);
        this.handleCover(OPEN_WOUND);
        this.handleFoodAcc(entity, OPEN_WOUND, 1.0f);
        this.handleInjuryInfection(OPEN_WOUND);
        this.handleCombatStimulant(entity, OPEN_WOUND);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(OPEN_WOUND).getValue() * Config.open_wound_bleed_ratio;
    }

    private void handlePassThrough(LivingEntity entity) {
        if (!this.abnormalWithHidden(PASS_THROUGH)) return;
        this.handleBandageAcc(PASS_THROUGH, Config.bandage_acc);
        this.handleCover(PASS_THROUGH);
        this.handleFoodAcc(entity, PASS_THROUGH, 1.0f);
        this.handleInjuryInfection(PASS_THROUGH);
        this.handleCombatStimulant(entity, PASS_THROUGH);

        if (isBandaged()) return;
        this.nextTickBleed += this.getCondition(PASS_THROUGH).getValue() * Config.open_wound_bleed_ratio * 1.5f;
    }

    private void handleBandageAcc(Identifier condition, float acc) {
        if (isBandaged()) {
            this.healingHidden(condition, - BodyCondition.get(condition).healingSpeed() * DELTA * (isBadBandaged() ? 1.0f : acc));
        }
    }

    protected void handleCover(Identifier condition) {
        ConditionState state = this.getCondition(condition);
        if (!isBandaged() && !isBadBandaged()) {
            this.setConditionValue(condition, state.getValue() + state.getHiddenValue());
            state.setHiddenValue(BodyCondition.get(condition).defaultValue());
        }
    }

    protected void handleFoodAcc(LivingEntity entity, Identifier condition, float acc) {
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

    private void handleInjuryInfection(Identifier condition) {
        ConditionState state = this.getCondition(condition);
        if (isBadBandaged()) {
            this.injury(INFECTION, BodyCondition.get(INFECTION).healingSpeed() * DELTA * 4 * state.getTotalValue());
        } else if (!isBandaged()) {
            this.injury(INFECTION, BodyCondition.get(INFECTION).healingSpeed() * DELTA * state.getTotalValue());
        }
    }

    private void handleInfection() {
        if (this.abnormal(OINTMENT))
            this.healing(INFECTION, -BodyCondition.get(INFECTION).healingSpeed() * 3 * DELTA);
        else if (this.isBandaged())
            this.healing(INFECTION, -BodyCondition.get(INFECTION).healingSpeed() * 2 * DELTA);
        else if (!this.isBadBandaged())
            this.healing(INFECTION, -BodyCondition.get(INFECTION).healingSpeed() * DELTA);
    }

    private void handleCombatStimulant(LivingEntity entity, Identifier condition) {
        if (!entity.hasEffect(ModEffects.COMBAT_STIMULANT_EFFECT)) return;
        if (this.abnormalOnlyHidden(condition)) {
            this.healingHidden(condition, -0.02f * DELTA);
        } else {
            this.healing(condition, -0.02f * DELTA);
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

    private void handleArterialBleeding(HealthCapability health) {
        if (this.abnormal(ARTERIAL_BLEEDING) && !this.abnormal(CLAMPED_ARTERIES)) {
            var blood = health.getComponent(BLOOD);
            blood.injury(BLOOD_LOSS, Config.fractureBloodRatio * DELTA);
        }
    }

    public boolean canHurtBone() {
        return !this.abnormalWithHidden(SAWED_BONES);
    }

    private void handleBoneDamage(HealthCapability health) {
        if (this.abnormal(SAWED_BONES)) return;
        var blood = health.getComponent(BLOOD);

        if (!this.canHurtBone()) return;
        if (blood.abnormal(OXYGEN)) {
            this.injury(BONE_DAMAGE, blood.getConditionValue(OXYGEN) * 1.1f * BodyCondition.get(BONE_DAMAGE).healingSpeed() * DELTA);
        }
        if (blood.abnormal(SEPSIS)) {
            this.injury(BONE_DAMAGE, blood.getConditionValue(SEPSIS) * 2 * BodyCondition.get(BONE_DAMAGE).healingSpeed() * DELTA);
        }
    }

    private void handleBoneDeath() {
        if (!this.canHurtBone()) return;
        if (this.getConditionValue(BONE_DAMAGE) > 0.9f) {
            this.injury(BONE_DEATH, BodyCondition.get(BONE_DEATH).maxValue());
        }

        if (!this.abnormal(BONE_DEATH) || this.abnormalWithHidden(FRACTURE)) {
            this.nextFractureTick = (int) (1200 + Utils.randomBetween(-1, 1) * 600);
        } else {
            this.nextFractureTick--;
            if (this.nextFractureTick <= 0) {
                this.injury(FRACTURE, BodyCondition.get(FRACTURE).maxValue());
            }
        }
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
            uuid_bone_stone = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_STONE);

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
            uuid_bone_copper = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_COPPER);

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
            uuid_bone_iron = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_IRON);

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
            uuid_bone_gold = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_GOLD);

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
            uuid_bone_dimond = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_DIMOND);

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
            uuid_bone_netherite = Common.getId(DontGetHurt.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

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

    public Identifier boneCrafted() {
        for (var key : BodyCondition.bones.keySet()) {
            if (this.abnormalWithHidden(key))
                return key;
        }
        return null;
    }

    public boolean isInfected() {
        return this.getConditionValue(INFECTION) > 0.1;
    }

    @Override
    public void serialize(@NotNull ValueOutput valueOutput) {
        super.serialize(valueOutput);
        valueOutput.putInt("nextFractureTick", this.nextFractureTick);
    }

    @Override
    public void deserialize(@NotNull ValueInput valueInput) {
        super.deserialize(valueInput);
        this.nextFractureTick = valueInput.getIntOr("nextFractureTick", 1200);
    }
}
