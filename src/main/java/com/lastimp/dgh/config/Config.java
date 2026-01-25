
package com.lastimp.dgh.config;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.2.10");

    public static final ForgeConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .comment("肢体血量系数")
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    public static final ForgeConfigSpec.BooleanValue TRADITION_HEALING = BUILDER
            .comment("启用伤口治疗，关闭生命盾")
            .define("TRADITION_HEALING", false);

    public static final ForgeConfigSpec.DoubleValue HEALING_FACTOR = BUILDER
            .comment("回血系数")
            .defineInRange("HEALING_FACTOR", 0.5f, 0, 1000);

    public static final ForgeConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .comment("绷带回复增益系数")
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ForgeConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .comment("烧伤出血系数")
            .defineInRange("BURN_BLEED_RATIO",0.5,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .comment("内伤出血系数")
            .defineInRange("INTERNAL_BLEED_RATIO",0.2,0,10);

    public static final ForgeConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .comment("开放伤出血系数")
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .comment("饱食度内伤恢复系数")
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .comment("出血-失血转化系数")
            .defineInRange("BLEED_VOLUME_RATIO",0.005,0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue WITHDRAW_RATIO = BUILDER
            .comment("成瘾-戒断转化系数")
            .defineInRange("WITHDRAW_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .comment("伤口自愈系数")
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .comment("药品有效时间系数")
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .comment("出血自愈系数")
            .defineInRange("VOLUME_SELF_HEALING_TIME",200,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue RESISTANCE_CONVERT_RATIO = BUILDER
            .comment("治愈-抗性转化系数")
            .defineInRange("RESISTANCE_CONVERT_RATIO", 0.02, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue RESISTANCE_MAX = BUILDER
            .comment("抗性上限")
            .defineInRange("RESISTANCE_MAX", 0.4, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_DISLOCATION_THRESHOLD = BUILDER
            .comment("伤口脱臼阈值")
            .defineInRange("BASE_DISLOCATION_THRESHOLD", 0.1, 0, 2.0);

    public static final ForgeConfigSpec.DoubleValue BASE_FRACTURE_THRESHOLD = BUILDER
            .comment("伤口骨折阈值")
            .defineInRange("BASE_FRACTURE_THRESHOLD", 0.25, 0, 2.0);

    public static final ForgeConfigSpec.DoubleValue BASE_DISLOCATION_MAX_PROB = BUILDER
            .comment("脱臼概率上限")
            .defineInRange("BASE_DISLOCATION_MAX_PROB", 0.8, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_FRACTURE_MAX_PROB = BUILDER
            .comment("骨折概率上限")
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.8, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue FRACTURE_ARTERIAL_PROB = BUILDER
            .comment("骨折-动脉出血概率")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.1, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue FRACTURE_BLOOD_RATIO = BUILDER
            .comment("动脉出血-失血速度")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.015, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BASE_PNEUMOTHORAX_PROB = BUILDER
            .comment("气胸概率")
            .defineInRange("BASE_PNEUMOTHORAX_PROB", 0.05, 0, 1);

    public static final ForgeConfigSpec.DoubleValue BASE_AMPUTATION_THRESHOLD = BUILDER
            .comment("截肢阈值")
            .defineInRange("BASE_AMPUTATION_THRESHOLD", 0.05, 0, 2.0);

    public static final ForgeConfigSpec.DoubleValue BASE_AMPUTATION_MAX_PROB = BUILDER
            .comment("截肢最大概率")
            .defineInRange("BASE_AMPUTATION_MAX_PROB", 0.3, 0, 1.0);

    public static final ForgeConfigSpec.BooleanValue ALLOW_DOWN = BUILDER
            .comment("允许濒死倒地")
            .define("ALLOW_DOWN", true);

    public static final ForgeConfigSpec.BooleanValue PLAYER_DOCTOR_HEALING = BUILDER
            .comment("允许医生村民治疗")
            .define("PLAYER_DOCTOR_HEALING", true);

    public static final ForgeConfigSpec.DoubleValue BYPASS_BRAIN_DAMAGE_PROB = BUILDER
            .comment("头部贯穿伤-脑损伤概率")
            .defineInRange("BYPASS_BRAIN_DAMAGE_PROB", 0.7, 0, 1.0);

    public static final ForgeConfigSpec.DoubleValue BYPASS_FOREIGN_PROB = BUILDER
            .comment("贯穿伤-体内异物概率")
            .defineInRange("BYPASS_FOREIGN_PROB", 0.8, 0, 1.0);

    public static final ForgeConfigSpec.BooleanValue PLAYER_GLOWING = BUILDER
            .comment("允许玩家倒地发光")
            .define("DOWN_GLOWING", true);

    public static final ForgeConfigSpec.BooleanValue PLAYER_DOWN_MOVING = BUILDER
            .comment("允许玩家倒地爬行")
            .define("PLAYER_DOWN_MOVING", true);

    public static final ForgeConfigSpec.IntValue DAMAGE_PART_STRICK_LEVEL = BUILDER
            .comment("伤害部位严格等级：0-随机部位，1-少量部位约束，2-大量部位约束，3-严格部位")
            .defineInRange("DAMAGE_PART_STRICK_LEVEL", 2, 0, 3);

    public static final ForgeConfigSpec.BooleanValue LIMITED_BODY_PART_VITALITY_LOST = BUILDER
            .comment("受限的部位生命损伤")
            .define("LIMITED_BODY_PART_VITALITY_LOST", true);

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
    public static boolean player_doctor_healing;

    public static float bypass_brain_damage_prob;
    public static float bypass_foreign_prob;
    public static boolean player_glowing;
    public static boolean player_down_moving;
    public static int damage_part_strick_level;
    public static boolean limited_body_part_vitality_lost;

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

        basePneumothoraxProb = (float) (double) BASE_PNEUMOTHORAX_PROB.get();
        baseAmputationThreshold = (float) (double) BASE_AMPUTATION_THRESHOLD.get();
        baseAmputationMaxProb = (float) (double) BASE_AMPUTATION_MAX_PROB.get();

        allow_down = ALLOW_DOWN.get();
        player_doctor_healing = PLAYER_DOCTOR_HEALING.get();

        bypass_brain_damage_prob = (float) (double) BYPASS_BRAIN_DAMAGE_PROB.get();
        bypass_foreign_prob = (float) (double) BYPASS_FOREIGN_PROB.get();
        player_glowing = PLAYER_GLOWING.get();
        player_down_moving = PLAYER_DOWN_MOVING.get();
        damage_part_strick_level = DAMAGE_PART_STRICK_LEVEL.get();
        limited_body_part_vitality_lost = LIMITED_BODY_PART_VITALITY_LOST.get();
        BodyCondition.init();
    }

}