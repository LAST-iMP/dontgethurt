package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.item.medicine.Sutures;
import com.lastimp.dgh.source.item.tool.Scalpel;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;

import static com.lastimp.dgh.DontGetHurt.EPS;

public class BodyCondition {
    public static final Map<ResourceLocation, Lazy<BodyCondition>> conditions = new HashMap<>();
    public static final List<ResourceLocation> bloodConditions = new LinkedList<>();
    public static final List<ResourceLocation> injuryConditions = new LinkedList<>();
    public static final List<ResourceLocation> surgeryConditions = new LinkedList<>();
    public static final List<ResourceLocation> painConditions = new LinkedList<>();
    public static final List<ResourceLocation> comfortConditions = new LinkedList<>();
    public static final List<ResourceLocation> resistConditions = new LinkedList<>();
    public static final Set<ResourceLocation> eyeVisible = new HashSet<>();
    public static final Map<ResourceLocation, RegistryObject<Item>> bones = new HashMap<>();

    public static ResourceLocation addCondition(ResourceLocation name, Function<ResourceLocation, BodyCondition> func) {
        conditions.put(name, Lazy.of(() -> func.apply(name)));
        return name;
    }

    public static ConditionBuilder create(ResourceLocation name) {
        return new ConditionBuilder(new BodyCondition(name));
    }

    public static BodyCondition get(ResourceLocation location) {
        return conditions.get(location).get();
    }

    private BodyCondition(ResourceLocation name) {
        this.name = name;
        this.texture = Common.ResourceLocation(name.getNamespace(), pathRoot + name.getPath() + ".png");
    }

    //肢体
    public static final ResourceLocation BURN = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "burn"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 0.25f)
                    .setValues(0.0f, 0.0f, 2.0f)
                    .isInjury().eyeVisible().build()
    );
    public static final ResourceLocation INTERNAL_INJURY = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "internal_injury"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 0.5f)
                    .setValues(0.0f, 0.0f, 2.0f)
                    .isInjury().build()
    );
    public static final ResourceLocation OPEN_WOUND = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "open_wound"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 0.25f)
                    .setValues(0.0f, 0.0f, 2.0f)
                    .isInjury().eyeVisible().build()
    );
    public static final ResourceLocation BLEED = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bleeding"),
            (name) -> create(name)
                    .setHealing(0.0f, 0.0f).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation INFECTION = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "infection"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 0.0f).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation FOREIGN_OBJECT = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "foreign_object"),
            (name) -> create(name)
                    .setHealing(0.0f, 0.0f).isInjury().build()
    );
    public static final ResourceLocation FRACTURE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "fracture"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time * 3, 0.0f).isPain().build()
    );
    public static final ResourceLocation INTENSE_PAIN = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "intense_pain"),
            (name) -> create(name)
                    .setHealing(0.2f, 1.0f).isPain().eyeVisible().build()
    );

    public static final ResourceLocation BANDAGED = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bandage"),
            (name) -> create(name)
                    .setHealing( 1.0f / Config.base_med_available_time, 0.0f).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation BANDAGED_DIRTY = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bandage_dirty"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation OINTMENT = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "ointment"),
            (name) -> create(name)
                    .setHealing( 1.0f / Config.base_med_available_time, 1.0f).isComfort().eyeVisible().build()
    );

    public static final ResourceLocation BURN_RES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "burn_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation INTERNAL_RES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "internal_injury_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation OPEN_WOUND_RES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "open_wound_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation BONE_DAMAGE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_damage"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time / 2, 1.0f).isPain().build()
    );
    public static final ResourceLocation BONE_DEATH = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_death"),
            (name) -> create(name)
                    .setHealing(0, 0).isPain().build()
    );
    //手术
    public static final ResourceLocation SURGERY_INCISION = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "scalpel"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation CLAMPED_BLEEDING = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "hemostat"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation RETRACTED_SKIN = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "retractor"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation DRILLED_BONES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "surgical_drill"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation SAWED_BONES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "sawed_bones"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation BONE_WOOD = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_wood"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_WOOD).setColor(0xFF7c3b19).build()
    );
    public static final ResourceLocation BONE_STONE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_stone"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_STONE).setColor(0xFF514b4d).build()
    );
    public static final ResourceLocation BONE_COPPER = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_copper"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_COPPER).setColor(0xFFFF6F00).build()
    );
    public static final ResourceLocation BONE_IRON = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_iron"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_IRON).setColor(0xFFCBC6BD).build()
    );
    public static final ResourceLocation BONE_GOLD = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_gold"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_GOLD).setColor(0xFFFEFF57).build()
    );
    public static final ResourceLocation BONE_DIMOND = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_dimond"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_DIMOND).setColor(0xFF3CFFF5).build()
    );
    public static final ResourceLocation BONE_NETHERITE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "bone_netherite"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isBone(ModItems.BONE_NETHERITE).setColor(0xFF845341).build()
    );
    //四肢
    public static final ResourceLocation DISLOCATION = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "dislocation"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isPain().eyeVisible().build()
    );
    public static final ResourceLocation PLASTER_CAST = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "plaster_cast"),
            (name) -> create(name)
                    .setHealing( 0.0f, 0.0f).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation ARTERIAL_BLEEDING = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "arterial_bleeding"),
            (name) -> create(name)
                    .setHealing(0, 0).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation CLAMPED_ARTERIES = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "clamped_arteries"),
            (name) -> create(name)
                    .setHealing(0, 0).isPain().eyeVisible().build()
    );
    public static final ResourceLocation GANGRENE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "gangrene"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 0).isInjury().eyeVisible().build()
    );
    //躯干
    public static final ResourceLocation ANALGESIA = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "analgesia"),
            (name) -> create(name)
                    .setHealing(1.0f/ Config.base_med_available_time, 1.0f).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation RESPIRATORY_ARREST= addCondition(Common.ResourceLocation(DontGetHurt.MODID, "respiratory_arrest"),
            (name) -> create(name)
                    .setHealing(0.5f, 1.0f).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation AORTIC_RUPTURE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "aortic_rupture"),
            (name) -> create(name)
                    .setHealing(0, 0).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation HEARTRATE_INCREASE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "heartrate_increase"),
            (name) -> create(name)
                    .setHealing(1.0f / 60, 0).isInjury().build()
    );
    public static final ResourceLocation HEARTRATE_IRREGULAR = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "heartrate_irregular"),
            (name) -> create(name)
                    .setHealing(1.0f / 40, 0).isInjury().build()
    );
    public static final ResourceLocation HEARTRATE_STOP = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "heartrate_stop"),
            (name) -> create(name)
                    .setHealing(1.0f / 20, 0).isInjury().build()
    );
    public static final ResourceLocation PNEUMOTHORAX = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "pneumothorax"),
            (name) -> create(name).setHealing(0, 0).isPain().build()
    );
    public static final ResourceLocation PNEUMOTHORAX_NEEDLE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "pneumothorax_needle"),
            (name) -> create(name).setHealing(1.0f/ Config.base_med_available_time, 1.0f).build()
    );
    //头脑
    public static final ResourceLocation WITHDRAW = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "withdraw"),
            (name) -> create(name)
                    .setHealing(1.0f/ Config.base_med_available_time, 1.0f).isInjury().build()
    );
    public static final ResourceLocation TRAUMATIC_SHOCK = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "traumatic_shock"),
            (name) -> create(name)
                    .setHealing(0.015f, 1.0f).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation BRAIN_DAMAGE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "brain_damage"),
            (name) -> create(name)
                    .setValues(0.0f, 0.0f, 2.0f)
                    .setHealing(0.001f, 2.0f).isInjury().build()
    );
    public static final ResourceLocation COMA = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "coma"),
            (name) -> create(name).setHealing(0.2f, 1.0f).eyeVisible().isInjury().build()
    );

    //血液
    public static final ResourceLocation SEPSIS = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "sepsis"),
            (name) -> create(name)
                    .setHealing(0.0095f / 2, 0.3f).isBlood().build()
    );
    public static final ResourceLocation HEMOTRANSFUSION = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "hemotransfusion"),
            (name) -> create(name)
                    .setHealing(0.0f, 0.0f).isBlood().build()
    );
    public static final ResourceLocation BLOOD_LOSS = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "blood_loss"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.volume_self_healing_time, 0.0f).isBlood().build()
    );
    public static final ResourceLocation BLOOD_PRESSURE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "blood_pressure"),
            (name) -> create(name)
                    .setValues(1.0f, 0.0f, 2.0f)
                    .setHealing(1.0f / Config.volume_self_healing_time, 0.0f).isBlood().build()
    );
    public static final ResourceLocation PH_LEVEL = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "ph_level"),
            (name) -> create(name)
                    .setHealing(0.0f, 0.0f).isBlood().build()
    );
    public static final ResourceLocation IMMUNITY = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "immunity"),
            (name) -> create(name)
                    .setHealing(0.0f, 0.0f).isBlood().build()
    );
    public static final ResourceLocation OPIATE_OVERDOSE = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "opiate_overdose"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time, 1.0f).isBlood().build()
    );
    public static final ResourceLocation OPIATE_ADDICTED = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "opiate_addicted"),
            (name) -> create(name)
                    .setHealing(1.0f / Config.base_self_healing_time / 5, 1.0f).build()
    );
    public static final ResourceLocation OXYGEN = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "oxygen"),
            (name) -> create(name)
                    .setHealing(0.05f, 0.0f).isBlood().build()
    );
    public static final ResourceLocation ANTIBIOTICS = addCondition(Common.ResourceLocation(DontGetHurt.MODID, "antibiotics"),
            (name) -> create(name)
                    .setHealing(0.005f, 1.0f).isBlood().build()
    );

    private static final String pathRoot = "textures/gui/sprites/container/condition_icons/";

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
            injuryConditions.add(this.instance.name);
            setColor(0xFFFF0000);
            return this;
        }

        public ConditionBuilder isSurgery() {
            this.instance.isPain = true;
            surgeryConditions.add(this.instance.name);
            setColor(0xFF89E9FF);
            return this;
        }

        public ConditionBuilder isPain() {
            this.instance.isPain = true;
            painConditions.add(this.instance.name);
            setColor(0xFFFFFF00);
            return this;
        }

        public ConditionBuilder isComfort() {
            this.instance.isComfort = true;
            comfortConditions.add(this.instance.name);
            setColor(0xFF00FF00);
            return this;
        }

        public ConditionBuilder isResist() {
            resistConditions.add(this.instance.name);
            setColor(0xFFF4FFA7);
            return this;
        }

        public ConditionBuilder isBone(RegistryObject<Item> bone) {
            this.isSurgery();
            bones.put(this.instance.name, bone);
            return this;
        }

        public ConditionBuilder isBlood() {
            bloodConditions.add(this.instance.name);
            return this;
        }

        public ConditionBuilder eyeVisible() {
            eyeVisible.add(this.instance.name);
            return this;
        }

        public BodyCondition build() {
            return this.instance;
        }
    }

    private static boolean initialized = false;
    public static void init() {
        if (initialized) return;
        for (var key : conditions.keySet()) {
            conditions.get(key).get();
        }

        AbstractVisibleBody.addCondition(List.of(
                SURGERY_INCISION,
                CLAMPED_BLEEDING,
                RETRACTED_SKIN,
                DRILLED_BONES,
                SAWED_BONES,

                BURN,
                INTERNAL_INJURY,
                OPEN_WOUND,
                BLEED,
                INFECTION,
                FOREIGN_OBJECT,
                BANDAGED,
                BANDAGED_DIRTY,
                OINTMENT,
                CLAMPED_ARTERIES,

                FRACTURE,
                INTENSE_PAIN,
                PLASTER_CAST,
                BONE_DAMAGE,
                BONE_DEATH,

                BONE_WOOD,
                BONE_STONE,
                BONE_COPPER,
                BONE_IRON,
                BONE_GOLD,
                BONE_DIMOND,
                BONE_NETHERITE,

                BURN_RES,
                INTERNAL_RES,
                OPEN_WOUND_RES
        ));

        AbstractExtremities.addCondition(List.of(
                DISLOCATION,
                ARTERIAL_BLEEDING,
                GANGRENE
        ));

        Head.addCondition(List.of(
                WITHDRAW,
                TRAUMATIC_SHOCK,
                BRAIN_DAMAGE,
                COMA
        ));

        Blood.addCondition(List.of(
                SEPSIS,
                HEMOTRANSFUSION,
                BLOOD_LOSS,
                BLOOD_PRESSURE,
                PH_LEVEL,
                IMMUNITY,

                OPIATE_OVERDOSE,
                OPIATE_ADDICTED,
                OXYGEN,
                ANTIBIOTICS
        ));

        Torso.addCondition(List.of(
                ANALGESIA,
                RESPIRATORY_ARREST,
                AORTIC_RUPTURE,
                HEARTRATE_INCREASE,
                HEARTRATE_IRREGULAR,
                HEARTRATE_STOP,
                PNEUMOTHORAX,

                PNEUMOTHORAX_NEEDLE
        ));

        Sutures.addCoverOnHeal(SAWED_BONES);
        Scalpel.addDiscoverOnHeal(SAWED_BONES);
        for (var key : bones.keySet()) {
            Sutures.addCoverOnHeal(key);
            Scalpel.addDiscoverOnHeal(key);
        }
        initialized = true;
    }
}


