
package com.lastimp.dgh.common.capability.bodyPart.bodies;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.utils.Utils;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.item.tool.SurgeryBones;
import com.lastimp.dgh.common.entry.register.ModEffects;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;
import static com.lastimp.dgh.common.enums.BodyComponents.*;

public class Torso extends AbstractVisibleBody {
    private static final Collection<ResourceLocation> uniqueConditions = new LinkedHashSet<>();
    private static List<ResourceLocation> TORSO_CONDITIONS;

    private AttributeInstance fly_speed;
    private AttributeInstance knock_back_resist;

    private ResourceLocation uuid_bone_wood;
    private ResourceLocation uuid_bone_netherite;

    private boolean heartStable = true;
    private int nextPneumothoraxTick = 1800;
    private int additionAir = 0;

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
    public BodyComponents getBodyType() {
        return TORSO;
    }

    @Override
    public Component getComponent() {
        return Component.literal("胸口");
    }

    @Override
    protected void initOrgan() {
        super.initOrgan();
        this.organ().addAllowed(ModTags.ORGAN_TORSO);
    }

    @Override
    public void addOriginOrgan(LivingEntity livingEntity, boolean newEntity) {
        this.insertOrganIfMissing(0, ORGAN_1_END, livingEntity, ModTags.HEART, ModItems.HEART.get().getDefaultInstance());
        this.insertOrganIfMissing(1, ORGAN_1_END, livingEntity, ModTags.LUNGS, ModItems.LUNGS.get().getDefaultInstance());
        this.insertOrganIfMissing(2, ORGAN_1_END, livingEntity, ModTags.STOMACH, ModItems.STOMACH.get().getDefaultInstance());
        this.insertOrganIfMissing(3, ORGAN_1_END, livingEntity, ModTags.LIVER, ModItems.LIVER.get().getDefaultInstance());
        if (!newEntity)
            this.insertOrganIfMissing(4, ORGAN_1_END, livingEntity, ModTags.KIDNEY, ModItems.KIDNEY.get().getDefaultInstance());
        else
            this.insertOrganIfMissing(4, ORGAN_1_END, 2, livingEntity, ModTags.KIDNEY, ModItems.KIDNEY.get().getDefaultInstance());
    }

    @Override
    public AbstractBody update(HealthCapability health, LivingEntity entity) {
        super.update(health, entity);
        this.handleAnalgesia(entity);
        this.handleRespiratoryArrest(health);
        this.handlePneumothorax();
        this.handleFibrillation(health, entity);
        return this;
    }

    @Override
    protected void updateOrgan(HealthCapability health, LivingEntity entity) {
        this.additionAir = 0;
        super.updateOrgan(health, entity);
    }

    @Override
    public float fractThreshold () {
        return PlatformService.CONFIG.BASE_FRACTURE_THRESHOLD() + 0.1f;
    }

    public boolean safeSurgery() {
        return this.abnormal(ANALGESIA);
    }

    @Override
    protected void updateBoneEffect(LivingEntity entity) {
        super.updateBoneEffect(entity);
        if (fly_speed == null) fly_speed = entity.getAttribute(Attributes.FLYING_SPEED);
        if (knock_back_resist == null) knock_back_resist = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);

        updateWoodBoneEffect();
        updateNetheriteBoneEffect();
    }

    @Override
    public int organ1BaseLevel() {
        return 6;
    }

    private void updateWoodBoneEffect() {
        if (uuid_bone_wood == null)
            uuid_bone_wood = ResourceHelper.ResourceLocation(Utils.MODID, this.getShortID() + "-" + SurgeryBones.ID_WOOD);

        if (this.getConditionHidden(BONE_WOOD) > ConditionAccessor.get(BONE_WOOD).maxValue() - Utils.EPS) {
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
            uuid_bone_netherite = ResourceHelper.ResourceLocation(Utils.MODID, this.getShortID() + "-" + SurgeryBones.ID_NETHERITE);

        if (this.getConditionHidden(BONE_NETHERITE) > ConditionAccessor.get(BONE_NETHERITE).maxValue() - Utils.EPS) {
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

    private void handleAnalgesia(LivingEntity entity) {
        if (entity.hasEffect(ModEffects.ADRENALINE_EFFECT) && this.getConditionValue(ANALGESIA) < 0.05f) {
            this.healing(ANALGESIA, ConditionAccessor.get(ANALGESIA).healingSpeed() * Utils.DELTA * 2);
        }
    }

    private void handleRespiratoryArrest(HealthCapability health) {
        var head = health.getComponent(HEAD);
        var blood = health.getComponent(BLOOD);
        if (this.abnormalOnlyHidden(SAWED_BONES) ||
                head.abnormal(CLAMPED_ARTERIES) ||
                blood.getConditionValue(OXYGEN) > 0.7 ||
                head.getConditionValue(TRAUMATIC_SHOCK) > 0.3 ||
                head.getConditionValue(BRAIN_DAMAGE) > 1 ||
                blood.getConditionValue(OPIATE_OVERDOSE) > 0.5 ||
                this.countOrganMatch(ModTags.LUNGS) < 1
        ) {
            this.injury(RESPIRATORY_ARREST, ConditionAccessor.get(RESPIRATORY_ARREST).maxValue());
        }
    }

    private void handlePneumothorax() {
        if (!this.abnormal(FRACTURE) || this.abnormal(PNEUMOTHORAX)) {
            nextPneumothoraxTick = (int) (1200 + Utils.randomBetween(-1, 1) * 600);
        } else {
            nextPneumothoraxTick--;
            if (nextPneumothoraxTick <= 0) {
                this.injury(PNEUMOTHORAX, ConditionAccessor.get(PNEUMOTHORAX).maxValue());
            }
        }

        if (this.abnormal(PNEUMOTHORAX) && !this.abnormal(PNEUMOTHORAX_NEEDLE)) {
            this.injury(PNEUMOTHORAX, Utils.DELTA * 0.05f);
        } else if (this.abnormal(PNEUMOTHORAX_NEEDLE) && this.abnormal(PNEUMOTHORAX)) {
            this.setConditionValue(PNEUMOTHORAX, 0.05f);
        }
    }

    private boolean isFibrillation(HealthCapability health, LivingEntity entity) {
        var blood = health.getComponent(BLOOD);
        return (entity.hasEffect(ModEffects.ADRENALINE_EFFECT) && entity.getEffect(ModEffects.ADRENALINE_EFFECT).getAmplifier() > 0) ||
                this.abnormal(AORTIC_RUPTURE) ||
                blood.getConditionValue(OXYGEN) > 0.10 ||
                blood.getConditionValue(BLOOD_PRESSURE) < 0.7;
    }

    private void handleFibrillation(HealthCapability health, LivingEntity entity) {
        var blood = health.getComponent(BLOOD);
        var head = health.getComponent(HEAD);
        this.heartStable = false;
        if (blood.getConditionValue(OXYGEN) > 0.9 || head.getConditionValue(TRAUMATIC_SHOCK) > 0.6 || this.countOrganMatch(ModTags.HEART) < 1) {
            this.addHeartRate(3.0f);
        } else if (this.isFibrillation(health, entity)) {
            float factor = entity.hasEffect(ModEffects.ADRENALINE_EFFECT) ? 0.5f : 1.0f;
            this.addHeartRate(Utils.DELTA / 60 * factor * (int)(Math.min(3, this.getHeartRateLevel() + 1)));
        } else if (this.getHeartRateLevel() < 0.5f && (blood.getConditionValue(SEPSIS) > 0.2 || blood.getConditionValue(BLOOD_LOSS) > 0.4 || this.getConditionValue(PNEUMOTHORAX) > 0.3f || entity.hasEffect(ModEffects.ADRENALINE_EFFECT))) {
            this.addHeartRate(Utils.DELTA / 20.0f);
        } else if (this.getHeartRateLevel() <= 2) {
            this.heartStable = true;
        }
    }

    public float getHeartRateLevel() {
        if (this.abnormal(HEARTRATE_STOP)) return 2 + this.getConditionValue(HEARTRATE_STOP);
        if (this.abnormal(HEARTRATE_IRREGULAR)) return 1 + this.getConditionValue(HEARTRATE_IRREGULAR);
        if (this.abnormal(HEARTRATE_INCREASE)) return this.getConditionValue(HEARTRATE_INCREASE);
        return 0;
    }

    public void setHeartRateLevel(float level) {
        level = Math.min(Math.max(0f, level), 3f);
        if (level > 2) {
            this.setConditionValue(HEARTRATE_STOP, level - 2);
            this.setConditionValue(HEARTRATE_IRREGULAR, ConditionAccessor.get(HEARTRATE_IRREGULAR).minValue());
            this.setConditionValue(HEARTRATE_INCREASE, ConditionAccessor.get(HEARTRATE_INCREASE).minValue());
        } else if (level > 1) {
            this.setConditionValue(HEARTRATE_STOP, ConditionAccessor.get(HEARTRATE_STOP).minValue());
            this.setConditionValue(HEARTRATE_IRREGULAR, level - 1);
            this.setConditionValue(HEARTRATE_INCREASE, ConditionAccessor.get(HEARTRATE_INCREASE).minValue());
        } else {
            this.setConditionValue(HEARTRATE_STOP, ConditionAccessor.get(HEARTRATE_STOP).minValue());
            this.setConditionValue(HEARTRATE_IRREGULAR, ConditionAccessor.get(HEARTRATE_IRREGULAR).minValue());
            this.setConditionValue(HEARTRATE_INCREASE, level);
        }
    }

    public void addHeartRate(float value) {
        this.setHeartRateLevel(getHeartRateLevel() + value);
    }

    @Override
    public void healing(ResourceLocation key, float value) {
        if (key == HEARTRATE_STOP || key == HEARTRATE_IRREGULAR || key == HEARTRATE_INCREASE) {
            this.addHeartRate(value);
        } else {
            super.healing(key, value);
        }
    }

    @Override
    public void injury(ResourceLocation key, float value) {
        if (key == HEARTRATE_STOP || key == HEARTRATE_IRREGULAR || key == HEARTRATE_INCREASE) {
            this.addHeartRate(value);
        } else {
            super.injury(key, value);
        }
    }

    @Override
    public @UnknownNullability CompoundTag serialize(HolderLookup.Provider provider) {
        var nbt = super.serialize(provider);
        nbt.putInt("nextPneumothoraxTick", nextPneumothoraxTick);
        return nbt;
    }

    @Override
    public void deserialize(HolderLookup.Provider provider, CompoundTag nbt) {
        this.nextPneumothoraxTick = nbt.getInt("nextPneumothoraxTick");
        super.deserialize(provider, nbt);
    }

    public boolean heartStable() {
        return heartStable;
    }

    public int additionAir() {
        return additionAir;
    }

    public void setAdditionAir(int additionAir) {
        this.additionAir = additionAir;
    }
}
