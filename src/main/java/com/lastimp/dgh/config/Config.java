
package com.lastimp.dgh.config;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.2.2");

    public static final ModConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .comment("肢体血量系数")
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    public static final ModConfigSpec.BooleanValue TRADITION_HEALING = BUILDER
            .comment("启用伤口治疗，关闭生命盾")
            .define("TRADITION_HEALING", false);

    public static final ModConfigSpec.DoubleValue HEALING_FACTOR = BUILDER
            .comment("回血系数")
            .defineInRange("HEALING_FACTOR", 0.5f, 0, 1000);

    public static final ModConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .comment("绷带回复增益系数")
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ModConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .comment("烧伤出血系数")
            .defineInRange("BURN_BLEED_RATIO",0.5,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .comment("内伤出血系数")
            .defineInRange("INTERNAL_BLEED_RATIO",0.2,0,10);

    public static final ModConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .comment("开放伤出血系数")
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .comment("饱食度内伤恢复系数")
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .comment("出血-失血转化系数")
            .defineInRange("BLEED_VOLUME_RATIO",0.005,0, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue WITHDRAW_RATIO = BUILDER
            .comment("成瘾-戒断转化系数")
            .defineInRange("WITHDRAW_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .comment("伤口自愈系数")
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .comment("药品有效时间系数")
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .comment("出血自愈系数")
            .defineInRange("VOLUME_SELF_HEALING_TIME",200,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue RESISTANCE_CONVERT_RATIO = BUILDER
            .comment("治愈-抗性转化系数")
            .defineInRange("RESISTANCE_CONVERT_RATIO", 0.01, 0, 1.0);

    public static final ModConfigSpec.DoubleValue RESISTANCE_MAX = BUILDER
            .comment("抗性上限")
            .defineInRange("RESISTANCE_MAX", 0.4, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_DISLOCATION_THRESHOLD = BUILDER
            .comment("伤口脱臼阈值")
            .defineInRange("BASE_DISLOCATION_THRESHOLD", 0.1, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_FRACTURE_THRESHOLD = BUILDER
            .comment("伤口骨折阈值")
            .defineInRange("BASE_FRACTURE_THRESHOLD", 0.25, 0, 0.7);

    public static final ModConfigSpec.DoubleValue BASE_DISLOCATION_MAX_PROB = BUILDER
            .comment("脱臼概率上限")
            .defineInRange("BASE_DISLOCATION_MAX_PROB", 0.8, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_FRACTURE_MAX_PROB = BUILDER
            .comment("骨折概率上限")
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.8, 0, 1.0);

    public static final ModConfigSpec.DoubleValue FRACTURE_ARTERIAL_PROB = BUILDER
            .comment("骨折-动脉出血概率")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.1, 0, 1.0);

    public static final ModConfigSpec.DoubleValue FRACTURE_BLOOD_RATIO = BUILDER
            .comment("动脉出血-失血速度")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.015, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BASE_PNEUMOTHORAX_PROB = BUILDER
            .comment("气胸概率")
            .defineInRange("BASE_PNEUMOTHORAX_PROB", 0.05, 0, 1);

    public static final ModConfigSpec.DoubleValue BASE_AMPUTATION_THRESHOLD = BUILDER
            .comment("截肢阈值")
            .defineInRange("BASE_AMPUTATION_THRESHOLD", 0.05, 0, 0.7);

    public static final ModConfigSpec.DoubleValue BASE_AMPUTATION_MAX_PROB = BUILDER
            .comment("截肢最大概率")
            .defineInRange("BASE_AMPUTATION_MAX_PROB", 0.3, 0, 1.0);

    public static final ModConfigSpec.BooleanValue ALLOW_DOWN = BUILDER
            .comment("允许濒死倒地")
            .define("ALLOW_DOWN", true);

    public static final ModConfigSpec.BooleanValue DOWN_DAMAGE_RESISTANCE = BUILDER
            .comment("允许濒死倒地减伤")
            .define("DOWN_DAMAGE_RESISTANCE", true);

    public static final ModConfigSpec.BooleanValue PLAYER_DOCTOR_HEALING = BUILDER
            .comment("允许医生村民治疗")
            .define("PLAYER_DOCTOR_HEALING", true);

    public static final ModConfigSpec.DoubleValue BYPASS_BRAIN_DAMAGE_PROB = BUILDER
            .comment("头部贯穿伤-脑损伤概率")
            .defineInRange("BYPASS_BRAIN_DAMAGE_PROB", 0.7, 0, 1.0);

    public static final ModConfigSpec.DoubleValue BYPASS_FOREIGN_PROB = BUILDER
            .comment("贯穿伤-体内异物概率")
            .defineInRange("BYPASS_FOREIGN_PROB", 0.8, 0, 1.0);

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

    public static float fractureArterialProb;
    public static float fractureBloodRatio;
    public static float basePneumothoraxProb;

    public static float baseAmputationThreshold;
    public static float baseAmputationMaxProb;

    public static boolean allow_down;
    public static boolean down_damage_resistance;
    public static boolean player_doctor_healing;

    public static float bypass_brain_damage_prob;
    public static float bypass_foreign_prob;

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

        basePneumothoraxProb = (float) BASE_PNEUMOTHORAX_PROB.getAsDouble();
        baseAmputationThreshold = (float) BASE_AMPUTATION_THRESHOLD.getAsDouble();
        baseAmputationMaxProb = (float) BASE_AMPUTATION_MAX_PROB.getAsDouble();

        allow_down = ALLOW_DOWN.get();
        down_damage_resistance = DOWN_DAMAGE_RESISTANCE.get();
        player_doctor_healing = PLAYER_DOCTOR_HEALING.get();

        bypass_brain_damage_prob = (float) (double) BYPASS_BRAIN_DAMAGE_PROB.get();
        bypass_foreign_prob = (float) (double) BYPASS_FOREIGN_PROB.get();
        BodyCondition.init();
    }

}