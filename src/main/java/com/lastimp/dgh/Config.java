
package com.lastimp.dgh;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.2.0");

    public static final ModConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    public static final ModConfigSpec.BooleanValue TRADITION_HEALING = BUILDER
            .define("TRADITION_HEALING", false);

    public static final ModConfigSpec.DoubleValue HEALING_FACTOR = BUILDER
            .defineInRange("BODY_LIFE_FACTOR", 0.5f, 0, 1000);

    public static final ModConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ModConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .defineInRange("BURN_BLEED_RATIO",0.5,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .defineInRange("INTERNAL_BLEED_RATIO",0.2,0,10);

    public static final ModConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .defineInRange("BLEED_VOLUME_RATIO",0.005,0, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue WITHDRAW_RATIO = BUILDER
            .defineInRange("WITHDRAW_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .defineInRange("VOLUME_SELF_HEALING_TIME",200,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue RESISTANCE_CONVERT_RATIO = BUILDER
            .defineInRange("RESISTANCE_CONVERT_RATIO", 0.01, 0, 1.0);

    public static final ModConfigSpec.DoubleValue RESISTANCE_MAX = BUILDER
            .defineInRange("RESISTANCE_MAX", 0.4, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_DISLOCATION_THRESHOLD = BUILDER
            .defineInRange("BASE_DISLOCATION_THRESHOLD", 0.1, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_FRACTURE_THRESHOLD = BUILDER
            .defineInRange("BASE_FRACTURE_THRESHOLD", 0.25, 0, 0.7);

    public static final ModConfigSpec.DoubleValue BASE_DISLOCATION_MAX_PROB = BUILDER
            .defineInRange("BASE_DISLOCATION_MAX_PROB", 0.8, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_FRACTURE_MAX_PROB = BUILDER
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.8, 0, 1.0);

    public static float body_life_factor;
    public static boolean tradition_healing;
    public static float healing_factor;
    public static float bandage_acc;
    public static float burn_bleed_ratio;
    public static float internal_bleed_ratio;
    public static float open_wound_bleed_ratio;
    public static float internal_food_healing;
    public static float bleed_volume_ratio;
    public static float withdraw_ratio;

    public static int base_self_healing_time;
    public static int base_med_available_time;
    public static int volume_self_healing_time;

    public static float resistance_convert_ratio;
    public static float resistance_max;

    public static float baseDislocationThreshold;
    public static float baseFractureThreshold;
    public static float baseDislocationMaxProb;
    public static float baseFractureMaxProb;


    public static final ModConfigSpec.DoubleValue FRACTURE_ARTERIAL_PROB = BUILDER
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.1, 0, 1.0);

    public static final ModConfigSpec.DoubleValue FRACTURE_BLOOD_RATIO = BUILDER
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.015, 0, 1.0);

    public static final ModConfigSpec.IntValue BASE_HEALING_SHIELD_TIME = BUILDER
            .defineInRange("BASE_FRACTURE_MAX_PROB", 100, 0, 10000);

    public static final ModConfigSpec.DoubleValue BASE_PNEUMOTHORAX_PROB = BUILDER
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.05, 0, 1);

    public static float fractureArterialProb;
    public static float fractureBloodRatio;
    public static int baseHealingShieldTime;
    public static float basePneumothoraxProb;

    // 构建配置
    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        body_life_factor = (float) BODY_LIFE_FACTOR.getAsDouble();
        tradition_healing = TRADITION_HEALING.getAsBoolean();
        healing_factor = (float) HEALING_FACTOR.getAsDouble();
        bandage_acc = (float) BANDAGE_ACC.getAsDouble();
        burn_bleed_ratio = (float) BURN_BLEED_RATIO.getAsDouble();
        internal_bleed_ratio = (float) INTERNAL_BLEED_RATIO.getAsDouble();
        open_wound_bleed_ratio = (float) OPEN_WOUND_BLEED_RATIO.getAsDouble();
        internal_food_healing = (float) INTERNAL_FOOD_HEALING.getAsDouble();
        bleed_volume_ratio = (float) BLEED_VOLUME_RATIO.getAsDouble();
        withdraw_ratio = (float) WITHDRAW_RATIO.getAsDouble();

        base_self_healing_time = BASE_SELF_HEALING_TIME.getAsInt();
        base_med_available_time = BASE_MED_AVAILABLE_TIME.getAsInt();
        volume_self_healing_time = VOLUME_SELF_HEALING_TIME.getAsInt();
        resistance_convert_ratio = (float) RESISTANCE_CONVERT_RATIO.getAsDouble();
        resistance_max = (float) RESISTANCE_MAX.getAsDouble();

        baseDislocationThreshold = (float) BASE_DISLOCATION_THRESHOLD.getAsDouble();
        baseFractureThreshold = (float) BASE_FRACTURE_THRESHOLD.getAsDouble();
        baseDislocationMaxProb = (float) BASE_DISLOCATION_MAX_PROB.getAsDouble();
        baseFractureMaxProb = (float) BASE_FRACTURE_MAX_PROB.getAsDouble();
        fractureArterialProb = (float) FRACTURE_ARTERIAL_PROB.getAsDouble();
        fractureBloodRatio = (float) FRACTURE_BLOOD_RATIO.getAsDouble();

        baseHealingShieldTime = BASE_HEALING_SHIELD_TIME.getAsInt();
        basePneumothoraxProb = (float) BASE_PNEUMOTHORAX_PROB.getAsDouble();
        BodyCondition.init();
    }

}