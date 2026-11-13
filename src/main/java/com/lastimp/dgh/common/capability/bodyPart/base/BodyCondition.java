package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.api.ComponentLevel;
import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.utils.ResourceHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.lastimp.dgh.api.DGH_API.registerCondition;
import static com.lastimp.dgh.common.utils.Utils.EPS;

public class BodyCondition {
    private static final String pathRoot = "textures/gui/sprites/container/condition_icons/";
    public final ResourceLocation name;
    public final ResourceLocation texture;
    private float defaultValue = 0f;
    private float minValue = 0f;
    private float maxValue = 1.0f;
    private float healingSpeed = 1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME();
    private float healingTS = 1.0f;
    private int color = 0xFFFF0000;
    private boolean isInjury = false;
    private boolean isPain = false;
    private boolean isComfort = false;
    private boolean isResist = false;
    private boolean isSelfHealing = false;
    private boolean eyeVisible = false;
    private boolean hidden = false;

    private BodyCondition(ResourceLocation name, ResourceLocation texture) {
        this.name = name;
        this.texture = texture;
    }

    private BodyCondition(ResourceLocation name) {
        this.name = name;
        this.texture = ResourceHelper.ResourceLocation(name.getNamespace(), pathRoot + name.getPath() + ".png");
    }

    private BodyCondition(BodyCondition condition) {
        this.name = condition.name;
        this.texture = condition.texture;
        this.defaultValue = condition.defaultValue;
        this.minValue = condition.minValue;
        this.maxValue = condition.maxValue;
        this.healingSpeed = condition.healingSpeed;
        this.healingTS = condition.healingTS;
        this.color = condition.color;
        this.isInjury = condition.isInjury;
        this.isPain = condition.isPain;
        this.isComfort = condition.isComfort;
        this.isResist = condition.isResist;
        this.isSelfHealing = condition.isSelfHealing;
        this.eyeVisible = condition.eyeVisible;
        this.hidden = condition.hidden;
    }

    public boolean abnormal(float value) {
        return defaultValue < value - EPS || defaultValue > value + EPS;
    }

    @Override
    public String toString() {
        return getComponent().getString();
    }

    public Component getComponent() {
        return Component.translatable(this.name());
    }

    public static ConditionBuilder create(ResourceLocation name, ResourceLocation texture, ComponentLevel components) {
        return new ConditionBuilder(name, texture, components);
    }

    public static class ConditionBuilder {
        private final BodyCondition instance;
        public final ComponentLevel components;

        public ConditionBuilder(ResourceLocation name, ResourceLocation texture, ComponentLevel components) {
            this.instance = new BodyCondition(name, texture);
            this.components = components;
        }

        public ConditionBuilder setValues(float defaultValue, float minValue, float maxValue) {
            this.instance.defaultValue = defaultValue;
            this.instance.minValue = minValue;
            this.instance.maxValue = maxValue;
            return this;
        }

        public ConditionBuilder setHealing(float healingSpeed, float healingTS) {
            this.instance.healingSpeed = healingSpeed;
            this.instance.healingTS = healingTS;
            return this;
        }

        public ConditionBuilder setHealing(float healingSpeed) {
            return setHealing(healingSpeed, 0);
        }

        public ConditionBuilder setColor(int color) {
            this.instance.color = color;
            return this;
        }

        public ConditionBuilder isInjury() {
            this.instance.isInjury = true;
            setColor(0xFFFF0000);
            return this;
        }

        public ConditionBuilder isSurgery() {
            this.instance.isPain = true;
            setColor(0xFF89E9FF);
            return this;
        }

        public ConditionBuilder isPain() {
            this.instance.isPain = true;
            setColor(0xFFFFFF00);
            return this;
        }

        public ConditionBuilder isComfort() {
            this.instance.isComfort = true;
            setColor(0xFF00FF00);
            return this;
        }

        public ConditionBuilder isResist() {
            this.instance.isResist = true;
            setColor(0xFF99ffa3);
            return this;
        }

        public ConditionBuilder isBlood() {
            this.instance.isPain = true;
            return this;
        }

        public ConditionBuilder eyeVisible() {
            this.instance.eyeVisible = true;
            return this;
        }

        public ConditionBuilder selfHealing() {
            this.instance.isSelfHealing = true;
            return this;
        }

        public ConditionBuilder isHidden() {
            this.instance.hidden = true;
            return this;
        }

        public BodyCondition build() {
            return new BodyCondition(this.instance);
        }
    }

    public boolean isInjury() {
        return this.isInjury;
    }

    public boolean isPain() {
        return this.isPain;
    }

    public boolean isComfort() {
        return this.isComfort;
    }

    public boolean isResist() {
        return isResist;
    }

    public boolean isSelfHealing() {
        return isSelfHealing;
    }

    public boolean hidden() {
        return hidden;
    }

    public boolean eyeVisible() {
        return eyeVisible;
    }

    public int color() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float healingSpeed() {
        return healingSpeed;
    }

    public float defaultValue() {
        return defaultValue;
    }

    public float healingTS() {
        return healingTS;
    }

    public float maxValue() {
        return maxValue;
    }

    public float minValue() {
        return minValue;
    }

    public String name() {
        return this.name.toString();
    }

    // 肢体
    public static final ResourceLocation BURN = ResourceHelper.ModResource("burn");
    public static final ResourceLocation INTERNAL_INJURY = ResourceHelper.ModResource("internal_injury");
    public static final ResourceLocation OPEN_WOUND = ResourceHelper.ModResource("open_wound");
    public static final ResourceLocation PASS_THROUGH = ResourceHelper.ModResource("pass_through");
    public static final ResourceLocation BLEED = ResourceHelper.ModResource("bleeding");
    public static final ResourceLocation INFECTION = ResourceHelper.ModResource("infection");
    public static final ResourceLocation FOREIGN_OBJECT = ResourceHelper.ModResource("foreign_object");
    public static final ResourceLocation FRACTURE = ResourceHelper.ModResource("fracture");
    public static final ResourceLocation INTENSE_PAIN = ResourceHelper.ModResource("intense_pain");

    public static final ResourceLocation BANDAGED = ResourceHelper.ModResource("bandage");
    public static final ResourceLocation BANDAGED_DIRTY = ResourceHelper.ModResource("bandage_dirty");
    public static final ResourceLocation HERB_BANDAGED = ResourceHelper.ModResource("herb_bandage");
    public static final ResourceLocation OINTMENT = ResourceHelper.ModResource("ointment");

    public static final ResourceLocation BURN_RES = ResourceHelper.ModResource("burn_resist");
    public static final ResourceLocation INTERNAL_RES = ResourceHelper.ModResource("internal_injury_resist");
    public static final ResourceLocation OPEN_WOUND_RES = ResourceHelper.ModResource("open_wound_resist");
    public static final ResourceLocation BONE_DAMAGE = ResourceHelper.ModResource("bone_damage");
    public static final ResourceLocation BONE_DEATH = ResourceHelper.ModResource("bone_death");
    public static final ResourceLocation ARTERIAL_BLEEDING = ResourceHelper.ModResource("arterial_bleeding");

    // 手术
    public static final ResourceLocation SURGERY_INCISION = ResourceHelper.ModResource("scalpel");
    public static final ResourceLocation CLAMPED_BLEEDING = ResourceHelper.ModResource("hemostat");
    public static final ResourceLocation RETRACTED_SKIN = ResourceHelper.ModResource("retractor");
    public static final ResourceLocation DRILLED_BONES = ResourceHelper.ModResource("surgical_drill");
    public static final ResourceLocation SAWED_BONES = ResourceHelper.ModResource("sawed_bones");

    public static final ResourceLocation BONE_WOOD = ResourceHelper.ModResource("bone_wood");
    public static final ResourceLocation BONE_STONE = ResourceHelper.ModResource("bone_stone");
    public static final ResourceLocation BONE_COPPER = ResourceHelper.ModResource("bone_copper");
    public static final ResourceLocation BONE_IRON = ResourceHelper.ModResource("bone_iron");
    public static final ResourceLocation BONE_GOLD = ResourceHelper.ModResource("bone_gold");
    public static final ResourceLocation BONE_DIMOND = ResourceHelper.ModResource("bone_dimond");
    public static final ResourceLocation BONE_NETHERITE = ResourceHelper.ModResource("bone_netherite");

    // 四肢
    public static final ResourceLocation DISLOCATION = ResourceHelper.ModResource("dislocation");
    public static final ResourceLocation PLASTER_CAST = ResourceHelper.ModResource("plaster_cast");
    public static final ResourceLocation CLAMP_PLATE = ResourceHelper.ModResource("clamp_plate");
    public static final ResourceLocation CLAMPED_ARTERIES = ResourceHelper.ModResource("clamped_arteries");
    public static final ResourceLocation GANGRENE = ResourceHelper.ModResource("gangrene");
    public static final ResourceLocation SURGICAL_AMPUTATION = ResourceHelper.ModResource("surgical_amputation");
    public static final ResourceLocation TRAUMATIC_AMPUTATION = ResourceHelper.ModResource("traumatic_amputation");

    // 躯干
    public static final ResourceLocation ANALGESIA = ResourceHelper.ModResource("analgesia");
    public static final ResourceLocation RESPIRATORY_ARREST = ResourceHelper.ModResource("respiratory_arrest");
    public static final ResourceLocation AORTIC_RUPTURE = ResourceHelper.ModResource("aortic_rupture");
    public static final ResourceLocation HEARTRATE_INCREASE = ResourceHelper.ModResource("heartrate_increase");
    public static final ResourceLocation HEARTRATE_IRREGULAR = ResourceHelper.ModResource("heartrate_irregular");
    public static final ResourceLocation HEARTRATE_STOP = ResourceHelper.ModResource("heartrate_stop");
    public static final ResourceLocation PNEUMOTHORAX = ResourceHelper.ModResource("pneumothorax");
    public static final ResourceLocation PNEUMOTHORAX_NEEDLE = ResourceHelper.ModResource("pneumothorax_needle");

    // 头脑
    public static final ResourceLocation WITHDRAW = ResourceHelper.ModResource("withdraw");
    public static final ResourceLocation TRAUMATIC_SHOCK = ResourceHelper.ModResource("traumatic_shock");
    public static final ResourceLocation BRAIN_DAMAGE = ResourceHelper.ModResource("brain_damage");
    public static final ResourceLocation COMA = ResourceHelper.ModResource("coma");

    // 血液
    public static final ResourceLocation SEPSIS = ResourceHelper.ModResource("sepsis");
    public static final ResourceLocation HEMOTRANSFUSION = ResourceHelper.ModResource("hemotransfusion");
    public static final ResourceLocation BLOOD_LOSS = ResourceHelper.ModResource("blood_loss");
    public static final ResourceLocation BLOOD_PRESSURE = ResourceHelper.ModResource("blood_pressure");
    public static final ResourceLocation PH_LEVEL = ResourceHelper.ModResource("ph_level");
    public static final ResourceLocation IMMUNITY = ResourceHelper.ModResource("immunity");
    public static final ResourceLocation OPIATE_OVERDOSE = ResourceHelper.ModResource("opiate_overdose");
    public static final ResourceLocation OPIATE_ADDICTED = ResourceHelper.ModResource("opiate_addicted");
    public static final ResourceLocation OXYGEN = ResourceHelper.ModResource("oxygen");
    public static final ResourceLocation ANTIBIOTICS = ResourceHelper.ModResource("antibiotics");
    public static final ResourceLocation HARDENER = ResourceHelper.ModResource("hardener");

    private static BodyCondition.ConditionBuilder register(ResourceLocation name, ComponentLevel components) {
        return registerCondition(name, ResourceHelper.ResourceLocation(name.getNamespace(), pathRoot + name.getPath()), components);
    }

    public static void registerModCondition() {
        float selfHealingTime = PlatformService.CONFIG.BASE_SELF_HEALING_TIME();
        float medicineTime = PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME();
        float volumeHealingTime = PlatformService.CONFIG.VOLUME_SELF_HEALING_TIME();

        // 通用可见身体部位
        register(BURN, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.5f / selfHealingTime, 0.25f)
                .setValues(0.0f, 0.0f, 2.01f)
                .isInjury().eyeVisible();

        register(INTERNAL_INJURY, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.5f / selfHealingTime, 0.5f)
                .setValues(0.0f, 0.0f, 2.01f)
                .isInjury();

        register(OPEN_WOUND, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.5f / selfHealingTime, 0.25f)
                .setValues(0.0f, 0.0f, 2.01f)
                .isInjury().eyeVisible();

        register(PASS_THROUGH, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.5f / selfHealingTime, 0.25f)
                .setValues(0.0f, 0.0f, 2.01f)
                .isInjury().eyeVisible();

        register(BLEED, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f);

        register(INFECTION, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / selfHealingTime)
                .isInjury().eyeVisible();

        register(FOREIGN_OBJECT, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isInjury();

        register(FRACTURE, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / selfHealingTime * 3)
                .isPain();

        register(INTENSE_PAIN, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.2f, 1.0f)
                .isPain().eyeVisible().selfHealing();

        register(BANDAGED, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / medicineTime)
                .isComfort().eyeVisible();

        register(BANDAGED_DIRTY, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isPain().eyeVisible();

        register(HERB_BANDAGED, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / medicineTime, 1.0f)
                .isComfort().selfHealing().eyeVisible();

        register(OINTMENT, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / medicineTime, 1.0f)
                .isComfort().selfHealing().eyeVisible();

        register(BURN_RES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isResist().eyeVisible();

        register(INTERNAL_RES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isResist().eyeVisible();

        register(OPEN_WOUND_RES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isResist().eyeVisible();

        register(BONE_DAMAGE, ComponentLevel.VISIBLE_BODY)
                .setHealing(1.0f / selfHealingTime / 2)
                .isPain();

        register(BONE_DEATH, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isPain();

        register(ARTERIAL_BLEEDING, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isInjury().eyeVisible();

        // 手术
        register(SURGERY_INCISION, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

        register(CLAMPED_BLEEDING, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

        register(RETRACTED_SKIN, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

        register(DRILLED_BONES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

        register(SAWED_BONES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

//        // 替换骨骼：这里只注册状况与显示属性
//        register(BONE_WOOD, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFF7C3B19);
//
//        register(BONE_STONE, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFF514B4D);
//
//        register(BONE_COPPER, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFFFF6F00);
//
//        register(BONE_IRON, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFFCBC6BD);
//
//        register(BONE_GOLD, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFFFEFF57);
//
//        register(BONE_DIMOND, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFF3CFFF5);
//
//        register(BONE_NETHERITE, ComponentLevel.VISIBLE_BODY)
//                .setHealing(0.0f)
//                .isSurgery().setColor(0xFF845341);

        register(PLASTER_CAST, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isComfort().eyeVisible();

        register(CLAMP_PLATE, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isComfort().eyeVisible();

        register(CLAMPED_ARTERIES, ComponentLevel.VISIBLE_BODY)
                .setHealing(0.0f)
                .isPain().eyeVisible();

        // 四肢
        register(DISLOCATION, ComponentLevel.EXTREMITIES)
                .setHealing(0.0f)
                .isPain().eyeVisible();

        register(GANGRENE, ComponentLevel.EXTREMITIES)
                .setHealing(1.0f / selfHealingTime)
                .isInjury().eyeVisible();

        register(SURGICAL_AMPUTATION, ComponentLevel.EXTREMITIES)
                .setHealing(0.0f)
                .isSurgery().eyeVisible();

        register(TRAUMATIC_AMPUTATION, ComponentLevel.EXTREMITIES)
                .setHealing(0.0f)
                .isInjury().eyeVisible();

        // 躯干
        register(ANALGESIA, ComponentLevel.TORSO)
                .setHealing(1.0f / medicineTime, 1.0f)
                .isComfort().selfHealing().eyeVisible();

        register(RESPIRATORY_ARREST, ComponentLevel.TORSO)
                .setHealing(0.5f, 1.0f)
                .isInjury().selfHealing().eyeVisible();

        register(AORTIC_RUPTURE, ComponentLevel.TORSO)
                .setHealing(0.0f)
                .isInjury().eyeVisible();

        register(HEARTRATE_INCREASE, ComponentLevel.TORSO)
                .setHealing(1.0f / 60)
                .isInjury();

        register(HEARTRATE_IRREGULAR, ComponentLevel.TORSO)
                .setHealing(1.0f / 40)
                .isInjury();

        register(HEARTRATE_STOP, ComponentLevel.TORSO)
                .setHealing(1.0f / 20)
                .isInjury();

        register(PNEUMOTHORAX, ComponentLevel.TORSO)
                .setHealing(0.0f)
                .isPain();

        register(PNEUMOTHORAX_NEEDLE, ComponentLevel.TORSO)
                .setHealing(1.0f / medicineTime, 1.0f)
                .selfHealing();

        // 头部
        register(WITHDRAW, ComponentLevel.HEAD)
                .setHealing(1.0f / medicineTime, 1.0f)
                .isInjury().selfHealing();

        register(TRAUMATIC_SHOCK, ComponentLevel.HEAD)
                .setHealing(0.015f, 1.0f)
                .isInjury().eyeVisible().selfHealing();

        register(BRAIN_DAMAGE, ComponentLevel.HEAD)
                .setValues(0.0f, 0.0f, 2.01f)
                .setHealing(0.001f)
                .isInjury();

        register(COMA, ComponentLevel.HEAD)
                .setHealing(0.2f, 1.0f)
                .eyeVisible().isInjury().selfHealing();

        // 血液
        register(SEPSIS, ComponentLevel.BLOOD)
                .setHealing(0.0095f / 2)
                .isBlood();

        register(HEMOTRANSFUSION, ComponentLevel.BLOOD)
                .setHealing(0.0f)
                .isBlood();

        register(BLOOD_LOSS, ComponentLevel.BLOOD)
                .setHealing(1.0f / volumeHealingTime)
                .isBlood();

        register(BLOOD_PRESSURE, ComponentLevel.BLOOD)
                .setValues(1.0f, 0.0f, 2.0f)
                .setHealing(1.0f / volumeHealingTime)
                .isBlood();

        register(PH_LEVEL, ComponentLevel.BLOOD)
                .setHealing(0.0f)
                .isBlood();

        register(IMMUNITY, ComponentLevel.BLOOD)
                .setHealing(0.0f)
                .isBlood();

        register(OPIATE_OVERDOSE, ComponentLevel.BLOOD)
                .setHealing(1.0f / selfHealingTime, 1.0f)
                .isBlood().selfHealing();

        register(OPIATE_ADDICTED, ComponentLevel.BLOOD)
                .setHealing(1.0f / selfHealingTime / 5, 1.0f)
                .isBlood().isHidden().selfHealing();

        register(OXYGEN, ComponentLevel.BLOOD)
                .setHealing(0.05f)
                .isBlood();

        register(ANTIBIOTICS, ComponentLevel.BLOOD)
                .setHealing(0.005f, 1.0f)
                .isBlood().selfHealing();

        register(HARDENER, ComponentLevel.BLOOD)
                .setHealing(0.01f, 1.0f)
                .isBlood().selfHealing();
    }
}


