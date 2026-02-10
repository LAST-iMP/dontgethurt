package com.lastimp.dgh.common.capability.bodyPart.base;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.utils.ResourceHelper;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import static com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor.addCondition;
import static com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor.*;
import static com.lastimp.dgh.common.utils.Utils.EPS;

public class BodyCondition {
    private static final String pathRoot = "textures/gui/sprites/container/condition_icons/";
    private final ResourceLocation name;
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

    private BodyCondition(ResourceLocation name) {
        this.name = name;
        this.texture = ResourceHelper.ResourceLocation(name.getNamespace(), pathRoot + name.getPath() + ".png");
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

    public static ConditionBuilder create(ResourceLocation name) {
        return new ConditionBuilder(new BodyCondition(name));
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

        public ConditionBuilder setHealing(float healingSpeed) {
            return setHealing(healingSpeed, 0);
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
            setColor(0xFF99ffa3);
            return this;
        }

        public ConditionBuilder isBone(IEntry<Item> bone) {
            this.isSurgery();
            bones.put(this.instance.name, bone);
            return this;
        }

        public ConditionBuilder isBlood() {
            this.instance.isPain = true;
            bloodConditions.add(this.instance.name);
            return this;
        }

        public ConditionBuilder eyeVisible() {
            eyeVisible.add(this.instance.name);
            return this;
        }

        public ConditionBuilder selfHealing() {
            selfHealing.add(this.instance.name);
            return this;
        }

        public BodyCondition build() {
            return this.instance;
        }
    }

    //肢体
    public static final ResourceLocation BURN = addCondition(ResourceHelper.ModResource("burn"),
            (name) -> create(name)
                    .setHealing(0.5f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME(), 0.25f)
                    .setValues(0.0f, 0.0f, 2.01f)
                    .isInjury().eyeVisible().build()
    );
    public static final ResourceLocation INTERNAL_INJURY = addCondition(ResourceHelper.ModResource("internal_injury"),
            (name) -> create(name)
                    .setHealing(0.5f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME(), 0.5f)
                    .setValues(0.0f, 0.0f, 2.01f)
                    .isInjury().build()
    );
    public static final ResourceLocation OPEN_WOUND = addCondition(ResourceHelper.ModResource("open_wound"),
            (name) -> create(name)
                    .setHealing(0.5f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME(), 0.25f)
                    .setValues(0.0f, 0.0f, 2.01f)
                    .isInjury().eyeVisible().build()
    );
    public static final ResourceLocation PASS_THROUGH = addCondition(ResourceHelper.ModResource("pass_through"),
            (name) -> create(name)
                    .setHealing(0.5f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME(), 0.25f)
                    .setValues(0.0f, 0.0f, 2.01f)
                    .isInjury().eyeVisible().build()
    );
    public static final ResourceLocation BLEED = addCondition(ResourceHelper.ModResource("bleeding"),
            (name) -> create(name)
                    .setHealing(0.0f).build()
    );
    public static final ResourceLocation INFECTION = addCondition(ResourceHelper.ModResource("infection"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME()).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation FOREIGN_OBJECT = addCondition(ResourceHelper.ModResource("foreign_object"),
            (name) -> create(name)
                    .setHealing(0.0f).isInjury().build()
    );
    public static final ResourceLocation FRACTURE = addCondition(ResourceHelper.ModResource("fracture"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME() * 3).isPain().build()
    );
    public static final ResourceLocation INTENSE_PAIN = addCondition(ResourceHelper.ModResource("intense_pain"),
            (name) -> create(name)
                    .setHealing(0.2f, 1.0f).isPain().eyeVisible().selfHealing().build()
    );

    public static final ResourceLocation BANDAGED = addCondition(ResourceHelper.ModResource("bandage"),
            (name) -> create(name)
                    .setHealing( 1.0f / PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME()).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation BANDAGED_DIRTY = addCondition(ResourceHelper.ModResource("bandage_dirty"),
            (name) -> create(name)
                    .setHealing( 0.0f).isPain().eyeVisible().build()
    );
    public static final ResourceLocation HERB_BANDAGED = addCondition(ResourceHelper.ModResource("herb_bandage"),
            (name) -> create(name)
                    .setHealing( 1.0f / PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME(), 1.0f).isComfort().selfHealing().eyeVisible().build()
    );
    public static final ResourceLocation OINTMENT = addCondition(ResourceHelper.ModResource("ointment"),
            (name) -> create(name)
                    .setHealing( 1.0f / PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME(), 1.0f).isComfort().selfHealing().eyeVisible().build()
    );

    public static final ResourceLocation BURN_RES = addCondition(ResourceHelper.ModResource("burn_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation INTERNAL_RES = addCondition(ResourceHelper.ModResource("internal_injury_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation OPEN_WOUND_RES = addCondition(ResourceHelper.ModResource("open_wound_resist"),
            (name) -> create(name)
                    .setHealing( 0.0f).isResist().eyeVisible().build()
    );
    public static final ResourceLocation BONE_DAMAGE = addCondition(ResourceHelper.ModResource("bone_damage"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME() / 2).isPain().build()
    );
    public static final ResourceLocation BONE_DEATH = addCondition(ResourceHelper.ModResource("bone_death"),
            (name) -> create(name)
                    .setHealing(0).isPain().build()
    );
    public static final ResourceLocation ARTERIAL_BLEEDING = addCondition(ResourceHelper.ModResource("arterial_bleeding"),
            (name) -> create(name)
                    .setHealing(0).isInjury().eyeVisible().build()
    );
    //手术
    public static final ResourceLocation SURGERY_INCISION = addCondition(ResourceHelper.ModResource("scalpel"),
            (name) -> create(name)
                    .setHealing( 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation CLAMPED_BLEEDING = addCondition(ResourceHelper.ModResource("hemostat"),
            (name) -> create(name)
                    .setHealing( 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation RETRACTED_SKIN = addCondition(ResourceHelper.ModResource("retractor"),
            (name) -> create(name)
                    .setHealing( 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation DRILLED_BONES = addCondition(ResourceHelper.ModResource("surgical_drill"),
            (name) -> create(name)
                    .setHealing( 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation SAWED_BONES = addCondition(ResourceHelper.ModResource("sawed_bones"),
            (name) -> create(name)
                    .setHealing( 0.0f).isSurgery().eyeVisible().build()
    );
    public static final ResourceLocation BONE_WOOD = addCondition(ResourceHelper.ModResource("bone_wood"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_WOOD).setColor(0xFF7c3b19).build()
    );
    public static final ResourceLocation BONE_STONE = addCondition(ResourceHelper.ModResource("bone_stone"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_STONE).setColor(0xFF514b4d).build()
    );
    public static final ResourceLocation BONE_COPPER = addCondition(ResourceHelper.ModResource("bone_copper"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_COPPER).setColor(0xFFFF6F00).build()
    );
    public static final ResourceLocation BONE_IRON = addCondition(ResourceHelper.ModResource("bone_iron"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_IRON).setColor(0xFFCBC6BD).build()
    );
    public static final ResourceLocation BONE_GOLD = addCondition(ResourceHelper.ModResource("bone_gold"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_GOLD).setColor(0xFFFEFF57).build()
    );
    public static final ResourceLocation BONE_DIMOND = addCondition(ResourceHelper.ModResource("bone_dimond"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_DIMOND).setColor(0xFF3CFFF5).build()
    );
    public static final ResourceLocation BONE_NETHERITE = addCondition(ResourceHelper.ModResource("bone_netherite"),
            (name) -> create(name)
                    .setHealing( 0.0f).isBone(ModItems.BONE_NETHERITE).setColor(0xFF845341).build()
    );
    //四肢
    public static final ResourceLocation DISLOCATION = addCondition(ResourceHelper.ModResource("dislocation"),
            (name) -> create(name)
                    .setHealing( 0.0f).isPain().eyeVisible().build()
    );
    public static final ResourceLocation PLASTER_CAST = addCondition(ResourceHelper.ModResource("plaster_cast"),
            (name) -> create(name)
                    .setHealing( 0.0f).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation CLAMP_PLATE = addCondition(ResourceHelper.ModResource("clamp_plate"),
            (name) -> create(name)
                    .setHealing( 0.0f).isComfort().eyeVisible().build()
    );
    public static final ResourceLocation CLAMPED_ARTERIES = addCondition(ResourceHelper.ModResource("clamped_arteries"),
            (name) -> create(name)
                    .setHealing(0).isPain().eyeVisible().build()
    );
    public static final ResourceLocation GANGRENE = addCondition(ResourceHelper.ModResource("gangrene"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME()).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation SURGICAL_AMPUTATION = addCondition(ResourceHelper.ModResource("surgical_amputation"),
            (name) -> create(name)
                    .setHealing(0).isSurgery().eyeVisible().build()
    );

    public static final ResourceLocation TRAUMATIC_AMPUTATION = addCondition(ResourceHelper.ModResource("traumatic_amputation"),
            (name) -> create(name)
                    .setHealing(0).isInjury().eyeVisible().build()
    );

    //躯干
    public static final ResourceLocation ANALGESIA = addCondition(ResourceHelper.ModResource("analgesia"),
            (name) -> create(name)
                    .setHealing(1.0f/ PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME(), 1.0f).isComfort().selfHealing().eyeVisible().build()
    );
    public static final ResourceLocation RESPIRATORY_ARREST= addCondition(ResourceHelper.ModResource("respiratory_arrest"),
            (name) -> create(name)
                    .setHealing(0.5f, 1.0f).isInjury().selfHealing().eyeVisible().build()
    );
    public static final ResourceLocation AORTIC_RUPTURE = addCondition(ResourceHelper.ModResource("aortic_rupture"),
            (name) -> create(name)
                    .setHealing(0).isInjury().eyeVisible().build()
    );
    public static final ResourceLocation HEARTRATE_INCREASE = addCondition(ResourceHelper.ModResource("heartrate_increase"),
            (name) -> create(name)
                    .setHealing(1.0f / 60).isInjury().build()
    );
    public static final ResourceLocation HEARTRATE_IRREGULAR = addCondition(ResourceHelper.ModResource("heartrate_irregular"),
            (name) -> create(name)
                    .setHealing(1.0f / 40).isInjury().build()
    );
    public static final ResourceLocation HEARTRATE_STOP = addCondition(ResourceHelper.ModResource("heartrate_stop"),
            (name) -> create(name)
                    .setHealing(1.0f / 20).isInjury().build()
    );
    public static final ResourceLocation PNEUMOTHORAX = addCondition(ResourceHelper.ModResource("pneumothorax"),
            (name) -> create(name).setHealing(0).isPain().build()
    );
    public static final ResourceLocation PNEUMOTHORAX_NEEDLE = addCondition(ResourceHelper.ModResource("pneumothorax_needle"),
            (name) -> create(name).setHealing(1.0f/ PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME(), 1.0f).selfHealing().build()
    );
    //头脑
    public static final ResourceLocation WITHDRAW = addCondition(ResourceHelper.ModResource("withdraw"),
            (name) -> create(name)
                    .setHealing(1.0f/ PlatformService.CONFIG.BASE_MED_AVAILABLE_TIME(), 1.0f).isInjury().selfHealing().build()
    );
    public static final ResourceLocation TRAUMATIC_SHOCK = addCondition(ResourceHelper.ModResource("traumatic_shock"),
            (name) -> create(name)
                    .setHealing(0.015f, 1.0f).isInjury().eyeVisible().selfHealing().build()
    );
    public static final ResourceLocation BRAIN_DAMAGE = addCondition(ResourceHelper.ModResource("brain_damage"),
            (name) -> create(name)
                    .setValues(0.0f, 0.0f, 2.01f)
                    .setHealing(0.001f).isInjury().build()
    );
    public static final ResourceLocation COMA = addCondition(ResourceHelper.ModResource("coma"),
            (name) -> create(name).setHealing(0.2f, 1.0f).eyeVisible().isInjury().selfHealing().build()
    );

    //血液
    public static final ResourceLocation SEPSIS = addCondition(ResourceHelper.ModResource("sepsis"),
            (name) -> create(name)
                    .setHealing(0.0095f / 2).isBlood().build()
    );
    public static final ResourceLocation HEMOTRANSFUSION = addCondition(ResourceHelper.ModResource("hemotransfusion"),
            (name) -> create(name)
                    .setHealing(0.0f).isBlood().build()
    );
    public static final ResourceLocation BLOOD_LOSS = addCondition(ResourceHelper.ModResource("blood_loss"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.VOLUME_SELF_HEALING_TIME()).isBlood().build()
    );
    public static final ResourceLocation BLOOD_PRESSURE = addCondition(ResourceHelper.ModResource("blood_pressure"),
            (name) -> create(name)
                    .setValues(1.0f, 0.0f, 2.0f)
                    .setHealing(1.0f / PlatformService.CONFIG.VOLUME_SELF_HEALING_TIME()).isBlood().build()
    );
    public static final ResourceLocation PH_LEVEL = addCondition(ResourceHelper.ModResource("ph_level"),
            (name) -> create(name)
                    .setHealing(0.0f).isBlood().build()
    );
    public static final ResourceLocation IMMUNITY = addCondition(ResourceHelper.ModResource("immunity"),
            (name) -> create(name)
                    .setHealing(0.0f).isBlood().build()
    );
    public static final ResourceLocation OPIATE_OVERDOSE = addCondition(ResourceHelper.ModResource("opiate_overdose"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME(), 1.0f).isBlood().selfHealing().build()
    );
    public static final ResourceLocation OPIATE_ADDICTED = addCondition(ResourceHelper.ModResource("opiate_addicted"),
            (name) -> create(name)
                    .setHealing(1.0f / PlatformService.CONFIG.BASE_SELF_HEALING_TIME() / 5, 1.0f).selfHealing().build()
    );
    public static final ResourceLocation OXYGEN = addCondition(ResourceHelper.ModResource("oxygen"),
            (name) -> create(name)
                    .setHealing(0.05f).isBlood().build()
    );
    public static final ResourceLocation ANTIBIOTICS = addCondition(ResourceHelper.ModResource("antibiotics"),
            (name) -> create(name)
                    .setHealing(0.005f, 1.0f).isBlood().selfHealing().build()
    );
    public static final ResourceLocation HARDENER = addCondition(ResourceHelper.ModResource("hardener"),
            (name) -> create(name)
                    .setHealing(0.01f, 1.0f).isBlood().selfHealing().build()
    );
}


