package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static com.lastimp.dgh.DontGetHurt.EPS;

public class BodyCondition {
    public static final Map<ResourceLocation, BodyCondition> conditions = new HashMap<>();

    public static ResourceLocation addCondition(BodyCondition instance) {
        conditions.put(instance.name, instance);
        return instance.name;
    }

    public static ConditionBuilder create(String id, String name) {
        return new ConditionBuilder(new BodyCondition(id, name));
    }

    public static BodyCondition get(ResourceLocation location) {
        return conditions.get(location);
    }

    //肢体
    public static final ResourceLocation BURN = addCondition(create(DontGetHurt.MODID, "burn")
            .setHealing(1.0f / Config.base_self_healing_time, 0.2f)
            .setValues(0.0f, 0.0f, 2.0f)
            .isInjury().build()
    );
    public static final ResourceLocation INTERNAL_INJURY = addCondition(create(DontGetHurt.MODID, "internal_injury")
            .setHealing(1.0f / Config.base_self_healing_time, 0.5f)
            .setValues(0.0f, 0.0f, 2.0f)
            .isInjury().build()
    );
    public static final ResourceLocation OPEN_WOUND = addCondition(create(DontGetHurt.MODID, "open_wound")
            .setHealing(1.0f / Config.base_self_healing_time, 0.5f)
            .setValues(0.0f, 0.0f, 2.0f)
            .isInjury().build()
    );
    public static final ResourceLocation BLEED = addCondition(create(DontGetHurt.MODID, "bleeding")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation INFECTION = addCondition(create(DontGetHurt.MODID, "infection")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation FOREIGN_OBJECT = addCondition(create(DontGetHurt.MODID, "foreign_object")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation FRACTURE = addCondition(create(DontGetHurt.MODID, "fracture")
            .setHealing(1.0f / Config.base_self_healing_time * 3, 0.0f).isPain().build()
    );
    public static final ResourceLocation INTENSE_PAIN = addCondition(create(DontGetHurt.MODID, "intense_pain")
            .setHealing(0.2f, 1.0f).isPain().build()
    );

    public static final ResourceLocation BANDAGED = addCondition(create(DontGetHurt.MODID, "bandage")
            .setHealing( 1.0f / Config.base_med_available_time, 0.0f).isComfort().build()
    );
    public static final ResourceLocation BANDAGED_DIRTY = addCondition(create(DontGetHurt.MODID, "bandage_dirty")
            .setHealing( 0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation OINTMENT = addCondition(create(DontGetHurt.MODID, "ointment")
            .setHealing( 1.0f / Config.base_med_available_time, 0.0f).isComfort().build()
    );

    public static final ResourceLocation BURN_RES = addCondition(create(DontGetHurt.MODID, "burn_resist")
            .setHealing( 0.0f, 0.0f).setColor(0xFFF4FFA7).build()
    );
    public static final ResourceLocation INTERNAL_RES = addCondition(create(DontGetHurt.MODID, "internal_injury_resist")
            .setHealing( 0.0f, 0.0f).setColor(0xFFF4FFA7).build()
    );
    public static final ResourceLocation OPEN_WOUND_RES = addCondition(create(DontGetHurt.MODID, "open_wound_resist")
            .setHealing( 0.0f, 0.0f).setColor(0xFFF4FFA7).build()
    );
    //手术
    public static final ResourceLocation SURGERY_INCISION = addCondition(create(DontGetHurt.MODID, "scalpel")
            .setHealing( 0.0f, 0.0f).isPain().setColor(0xFF89E9FF).build()
    );
    public static final ResourceLocation CLAMPED_BLEEDING = addCondition(create(DontGetHurt.MODID, "hemostat")
            .setHealing( 0.0f, 0.0f).isPain().setColor(0xFF89E9FF).build()
    );
    public static final ResourceLocation RETRACTED_SKIN = addCondition(create(DontGetHurt.MODID, "retractor")
            .setHealing( 0.0f, 0.0f).isPain().setColor(0xFF89E9FF).build()
    );
    public static final ResourceLocation DRILLED_BONES = addCondition(create(DontGetHurt.MODID, "surgical_drill")
            .setHealing( 0.0f, 0.0f).isPain().setColor(0xFF89E9FF).build()
    );
    //四肢
    public static final ResourceLocation DISLOCATION = addCondition(create(DontGetHurt.MODID, "dislocation")
            .setHealing( 0.0f, 0.0f).isPain().build()
    );
    public static final ResourceLocation PLASTER_CAST = addCondition(create(DontGetHurt.MODID, "plaster_cast")
            .setHealing( 0.0f, 0.0f).isComfort().build()
    );
    //躯干
    public static final ResourceLocation ANALGESIA = addCondition(create(DontGetHurt.MODID, "analgesia")
            .setHealing(1.0f/ Config.base_med_available_time, 1.0f).isComfort().build()
    );
    public static final ResourceLocation RESPIRATORY_ARREST= addCondition(create(DontGetHurt.MODID, "respiratory_arrest")
            .setHealing(0.5f, 1.0f).isInjury().build()
    );
    //头脑
    public static final ResourceLocation WITHDRAW = addCondition(create(DontGetHurt.MODID, "withdraw")
            .setHealing(1.0f/ Config.base_med_available_time, 1.0f).isInjury().build()
    );
    public static final ResourceLocation TRAUMATIC_SHOCK = addCondition(create(DontGetHurt.MODID, "traumatic_shock")
            .setHealing(0.015f, 1.0f).isInjury().build()
    );
    public static final ResourceLocation BRAIN_DAMAGE = addCondition(create(DontGetHurt.MODID, "brain_damage")
            .setValues(0.0f, 0.0f, 2.0f)
            .setHealing(0.001f, 2.0f).isInjury().build()
    );

    //血液
    public static final ResourceLocation SEPSIS = addCondition(create(DontGetHurt.MODID, "sepsis")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation HEMOTRANSFUSION = addCondition(create(DontGetHurt.MODID, "hemotransfusion")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation BLOOD_LOSS = addCondition(create(DontGetHurt.MODID, "blood_loss")
            .setHealing(1.0f / Config.volume_self_healing_time, 0.0f).isInjury().build()
    );
    public static final ResourceLocation BLOOD_PRESSURE = addCondition(create(DontGetHurt.MODID, "blood_pressure")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation PH_LEVEL = addCondition(create(DontGetHurt.MODID, "ph_level")
            .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation IMMUNITY = addCondition(create(DontGetHurt.MODID, "immunity")
            .setHealing(0.0f, 0.0f).isComfort().build()
    );
    public static final ResourceLocation OPIATE_OVERDOSE = addCondition(create(DontGetHurt.MODID, "opiate_overdose")
            .setHealing(1.0f / Config.base_self_healing_time, 1.0f).isInjury().build()
    );
    public static final ResourceLocation OPIATE_ADDICTED = addCondition(create(DontGetHurt.MODID, "opiate_addicted")
            .setHealing(1.0f / Config.base_self_healing_time / 5, 1.0f).isInjury().build()
    );
    public static final ResourceLocation OXYGEN = addCondition(create(DontGetHurt.MODID, "oxygen")
            .setHealing(0.05f, 0.0f).isInjury().build()
    );

    public static final String pathRoot = "container/condition_icons/";

    private final ResourceLocation name;
    public final ResourceLocation texture;

    private float defaultValue = 0f;
    private float minValue = 0f;
    private float maxValue = 1.0f;

    private float healingSpeed = 1.0f / Config.base_self_healing_time;
    private float healingTS = 1.0f;

    private int color = 0xFFFF0000;

    private boolean isInjury = false;
    private boolean isPain = false;
    private boolean isComfort = false;

    private BodyCondition(String id, String name) {
        this.name = Common.ResourceLocation(id, name);
        this.texture = Common.ResourceLocation(id, pathRoot + name);
    }

    public boolean abnormal(float value) {
        return defaultValue < value - EPS || defaultValue > value + EPS;
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

    public int color() {
        return color;
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

    @Override
    public String toString() {
        return getComponent().getString();
    }

    public Component getComponent() {
        return Component.translatable(this.name());
    }

    record ConditionBuilder(BodyCondition instance) {
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

            public ConditionBuilder setColor(int color) {
                this.instance.color = color;
                return this;
            }

            public ConditionBuilder isInjury() {
                this.instance.isInjury = true;
                this.instance.isPain = false;
                this.instance.isComfort = false;
                setColor(0xFFFF0000);
                return this;
            }

            public ConditionBuilder isPain() {
                this.instance.isInjury = false;
                this.instance.isPain = true;
                this.instance.isComfort = false;
                setColor(0xFFFFFF00);
                return this;
            }

            public ConditionBuilder isComfort() {
                this.instance.isInjury = false;
                this.instance.isPain = false;
                this.instance.isComfort = true;
                setColor(0xFF00FF00);
                return this;
            }

            public BodyCondition build() {
                return this.instance;
            }
        }
}


