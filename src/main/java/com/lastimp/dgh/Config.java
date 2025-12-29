
package com.lastimp.dgh;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.2.0");

    public static final ForgeConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    public static final ForgeConfigSpec.BooleanValue TRADITION_HEALING = BUILDER
            .define("TRADITION_HEALING", false);

    public static final ForgeConfigSpec.DoubleValue HEALING_FACTOR = BUILDER
            .defineInRange("BODY_LIFE_FACTOR", 0.5f, 0, 1000);

    public static final ForgeConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ForgeConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .defineInRange("BURN_BLEED_RATIO",0.5,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .defineInRange("INTERNAL_BLEED_RATIO",0.2,0,10);

    public static final ForgeConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .defineInRange("BLEED_VOLUME_RATIO",0.005,0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue WITHDRAW_RATIO = BUILDER
            .defineInRange("WITHDRAW_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .defineInRange("VOLUME_SELF_HEALING_TIME",200,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue RESISTANCE_CONVERT_RATIO = BUILDER
            .defineInRange("RESISTANCE_CONVERT_RATIO", 0.01, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue RESISTANCE_MAX = BUILDER
            .defineInRange("RESISTANCE_MAX", 0.4, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_DISLOCATION_THRESHOLD = BUILDER
            .defineInRange("BASE_DISLOCATION_THRESHOLD", 0.1, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_FRACTURE_THRESHOLD = BUILDER
            .defineInRange("BASE_FRACTURE_THRESHOLD", 0.25, 0, 0.7);

    public static final ForgeConfigSpec.DoubleValue BASE_DISLOCATION_MAX_PROB = BUILDER
            .defineInRange("BASE_DISLOCATION_MAX_PROB", 0.8, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_FRACTURE_MAX_PROB = BUILDER
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


    public static final ForgeConfigSpec.DoubleValue FRACTURE_ARTERIAL_PROB = BUILDER
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.1, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue FRACTURE_BLOOD_RATIO = BUILDER
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.015, 0, 1.0);

    public static final ForgeConfigSpec.IntValue BASE_HEALING_SHIELD_TIME = BUILDER
            .defineInRange("BASE_FRACTURE_MAX_PROB", 100, 0, 10000);

    public static final ForgeConfigSpec.DoubleValue BASE_PNEUMOTHORAX_PROB = BUILDER
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.05, 0, 1);

    public static final ForgeConfigSpec.DoubleValue BASE_AMPUTATION_THRESHOLD = BUILDER
            .defineInRange("BASE_AMPUTATION_THRESHOLD", 0.05, 0, 0.7);

    public static final ForgeConfigSpec.DoubleValue BASE_AMPUTATION_MAX_PROB = BUILDER
            .defineInRange("BASE_AMPUTATION_MAX_PROB", 0.3, 0, 1.0);

    public static float fractureArterialProb;
    public static float fractureBloodRatio;
    public static int baseHealingShieldTime;
    public static float basePneumothoraxProb;

    public static float baseAmputationThreshold;
    public static float baseAmputationMaxProb;

    // 构建配置
    public static final ForgeConfigSpec SPEC = BUILDER.pop().build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        body_life_factor = (float) (double)BODY_LIFE_FACTOR.get();
        tradition_healing = TRADITION_HEALING.get();
        healing_factor = (float) (double)HEALING_FACTOR.get();
        bandage_acc = (float) (double)BANDAGE_ACC.get();
        burn_bleed_ratio = (float) (double) BURN_BLEED_RATIO.get();
        internal_bleed_ratio = (float) (double) INTERNAL_BLEED_RATIO.get();
        open_wound_bleed_ratio = (float) (double) OPEN_WOUND_BLEED_RATIO.get();
        internal_food_healing = (float) (double) INTERNAL_FOOD_HEALING.get();
        bleed_volume_ratio = (float) (double) BLEED_VOLUME_RATIO.get();
        withdraw_ratio = (float) (double) WITHDRAW_RATIO.get();

        base_self_healing_time = BASE_SELF_HEALING_TIME.get();
        base_med_available_time = BASE_MED_AVAILABLE_TIME.get();
        volume_self_healing_time = VOLUME_SELF_HEALING_TIME.get();
        resistance_convert_ratio = (float) (double) RESISTANCE_CONVERT_RATIO.get();
        resistance_max = (float) (double) RESISTANCE_MAX.get();

        baseDislocationThreshold = (float) (double) BASE_DISLOCATION_THRESHOLD.get();
        baseFractureThreshold = (float) (double) BASE_FRACTURE_THRESHOLD.get();
        baseDislocationMaxProb = (float) (double) BASE_DISLOCATION_MAX_PROB.get();
        baseFractureMaxProb = (float) (double) BASE_FRACTURE_MAX_PROB.get();
        fractureArterialProb = (float) (double) FRACTURE_ARTERIAL_PROB.get();
        fractureBloodRatio = (float) (double) FRACTURE_BLOOD_RATIO.get();

        baseHealingShieldTime = BASE_HEALING_SHIELD_TIME.get();
        basePneumothoraxProb = (float) (double) BASE_PNEUMOTHORAX_PROB.get();
        baseAmputationThreshold = (float) (double) BASE_AMPUTATION_THRESHOLD.get();
        baseAmputationMaxProb = (float) (double) BASE_AMPUTATION_MAX_PROB.get();
        BodyCondition.init();
    }

}