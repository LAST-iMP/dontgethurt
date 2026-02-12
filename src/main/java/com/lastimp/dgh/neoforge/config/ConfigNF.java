
package com.lastimp.dgh.neoforge.config;

import com.lastimp.dgh.common.config.IConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public class ConfigNF implements IConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.3.3");

    private static final ModConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .comment("肢体血量系数")
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    private static final ModConfigSpec.BooleanValue TRADITION_HEALING = BUILDER
            .comment("启用伤口治疗，关闭生命盾")
            .define("TRADITION_HEALING", false);

    private static final ModConfigSpec.DoubleValue HEALTH_SHIELD_REDU = BUILDER
            .comment("生命盾衰减率(越大越慢)")
            .defineInRange("HEALTH_SHIELD_REDU", 60f, 1, Float.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue HEALING_FACTOR = BUILDER
            .comment("回血系数")
            .defineInRange("HEALING_FACTOR", 0.5f, 0, 1000);

    private static final ModConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .comment("绷带回复增益系数")
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    private static final ModConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .comment("烧伤出血系数")
            .defineInRange("BURN_BLEED_RATIO",0.5,0,10);

    private static final ModConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .comment("内伤出血系数")
            .defineInRange("INTERNAL_BLEED_RATIO",0.2,0,10);

    private static final ModConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .comment("开放伤出血系数")
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    private static final ModConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .comment("饱食度恢复系数")
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .comment("出血-失血转化系数")
            .defineInRange("BLEED_VOLUME_RATIO",0.005,0, Float.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WITHDRAW_RATIO = BUILDER
            .comment("成瘾-戒断转化系数")
            .defineInRange("WITHDRAW_RATIO",0.03,0, Float.MAX_VALUE);

    private static final ModConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .comment("伤口自愈系数")
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .comment("药品有效时间系数")
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .comment("出血自愈系数")
            .defineInRange("VOLUME_SELF_HEALING_TIME",200,1, Integer.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue RESISTANCE_CONVERT_RATIO = BUILDER
            .comment("治愈-抗性转化系数")
            .defineInRange("RESISTANCE_CONVERT_RATIO", 0.02, 0, 1.0);

    private static final ModConfigSpec.DoubleValue RESISTANCE_MAX = BUILDER
            .comment("抗性上限")
            .defineInRange("RESISTANCE_MAX", 0.4, 0, 1.0);

    private static final ModConfigSpec.DoubleValue BASE_DISLOCATION_THRESHOLD = BUILDER
            .comment("伤口脱臼阈值")
            .defineInRange("BASE_DISLOCATION_THRESHOLD", 0.1, 0, 2.0);

    private static final ModConfigSpec.DoubleValue BASE_FRACTURE_THRESHOLD = BUILDER
            .comment("伤口骨折阈值")
            .defineInRange("BASE_FRACTURE_THRESHOLD", 0.25, 0, 2.0);

    private static final ModConfigSpec.DoubleValue BASE_DISLOCATION_MAX_PROB = BUILDER
            .comment("脱臼概率上限")
            .defineInRange("BASE_DISLOCATION_MAX_PROB", 0.8, 0, 1.0);

    private static final ModConfigSpec.DoubleValue BASE_FRACTURE_MAX_PROB = BUILDER
            .comment("骨折概率上限")
            .defineInRange("BASE_FRACTURE_MAX_PROB", 0.8, 0, 1.0);

    private static final ModConfigSpec.DoubleValue FRACTURE_ARTERIAL_PROB = BUILDER
            .comment("骨折-动脉出血概率")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.1, 0, 1.0);

    private static final ModConfigSpec.DoubleValue FRACTURE_BLOOD_RATIO = BUILDER
            .comment("动脉出血-失血速度")
            .defineInRange("FRACTURE_ARTERIAL_PROB", 0.015, 0, 1.0);

    private static final ModConfigSpec.DoubleValue BASE_PNEUMOTHORAX_PROB = BUILDER
            .comment("气胸概率")
            .defineInRange("BASE_PNEUMOTHORAX_PROB", 0.05, 0, 1);

    private static final ModConfigSpec.DoubleValue BASE_AMPUTATION_THRESHOLD = BUILDER
            .comment("截肢阈值")
            .defineInRange("BASE_AMPUTATION_THRESHOLD", 0.05, 0, 2.0);

    private static final ModConfigSpec.DoubleValue BASE_AMPUTATION_MAX_PROB = BUILDER
            .comment("截肢最大概率")
            .defineInRange("BASE_AMPUTATION_MAX_PROB", 0.3, 0, 1.0);

    private static final ModConfigSpec.BooleanValue ALLOW_DOWN = BUILDER
            .comment("允许濒死倒地")
            .define("ALLOW_DOWN", true);

    private static final ModConfigSpec.BooleanValue PLAYER_DOCTOR_HEALING = BUILDER
            .comment("允许医生村民治疗")
            .define("PLAYER_DOCTOR_HEALING", true);

    private static final ModConfigSpec.DoubleValue BYPASS_BRAIN_DAMAGE_PROB = BUILDER
            .comment("头部贯穿伤-脑损伤概率")
            .defineInRange("BYPASS_BRAIN_DAMAGE_PROB", 0.7, 0, 1.0);

    private static final ModConfigSpec.DoubleValue BYPASS_FOREIGN_PROB = BUILDER
            .comment("贯穿伤-体内异物概率")
            .defineInRange("BYPASS_FOREIGN_PROB", 0.8, 0, 1.0);

    private static final ModConfigSpec.BooleanValue PLAYER_GLOWING = BUILDER
            .comment("允许玩家倒地发光")
            .define("DOWN_GLOWING", true);

    private static final ModConfigSpec.BooleanValue PLAYER_DOWN_MOVING = BUILDER
            .comment("允许玩家倒地爬行")
            .define("PLAYER_DOWN_MOVING", true);

    private static final ModConfigSpec.IntValue DAMAGE_PART_STRICK_LEVEL = BUILDER
            .comment("伤害部位严格等级：0-随机部位，1-少量部位约束，2-大量部位约束，3-严格部位")
            .defineInRange("DAMAGE_PART_STRICK_LEVEL", 2, 0, 3);

    private static final ModConfigSpec.BooleanValue LIMITED_BODY_PART_VITALITY_LOST = BUILDER
            .comment("受限的部位生命损伤")
            .define("LIMITED_BODY_PART_VITALITY_LOST", true);

    private static final ModConfigSpec.BooleanValue ENABLE_LIVING_EFFECT = BUILDER
            .comment("启用长生久视buff")
            .define("ENABLE_LIVING_EFFECT", true);

    private static final ModConfigSpec.BooleanValue ENABLE_SELF_SUICIDE = BUILDER
            .comment("启用放弃治疗")
            .define("ENABLE_SELF_SUICIDE", true);

    private static final ModConfigSpec.IntValue SMALL_CONDITION_X = BUILDER
            .comment("健康小人ui的x位置")
            .defineInRange("SMALL_CONDITION_X", 308, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SMALL_CONDITION_Y = BUILDER
            .comment("健康小人ui的y位置")
            .defineInRange("SMALL_CONDITION_Y", 214, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue SMALL_CONDITION_DISAPPEAR_DELAY = BUILDER
            .comment("健康小人ui的渐隐时间(-1为永不消失, -2为永不显示)")
            .defineInRange("SMALL_CONDITION_DISAPPEAR_DELAY", 3, -2, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue ARMOR_RECALCULATE = BUILDER
            .comment("启用改型护甲计算")
            .define("ARMOR_RECALCULATE", true);

    private static final ModConfigSpec.DoubleValue BLOCK_RECOVER_DELAY = BUILDER
            .comment("护甲格挡恢复延迟")
            .defineInRange("BLOCK_RECOVER_DELAY", 5, 0, Float.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue BLOCK_RECOVER_SPEED = BUILDER
            .comment("护甲格挡恢复速度")
            .defineInRange("BLOCK_RECOVER_SPEED", 0.1, 0, 1.0);
    // 构建配置
    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    @Override
    public Path getConfigRoot() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public float BODY_LIFE_FACTOR() {
        return (float) BODY_LIFE_FACTOR.getAsDouble();
    }

    @Override
    public boolean TRADITION_HEALING() {
        return TRADITION_HEALING.getAsBoolean();
    }

    @Override
    public float HEALTH_SHIELD_REDU() {
        return (float) HEALTH_SHIELD_REDU.getAsDouble();
    }

    @Override
    public float HEALING_FACTOR() {
        return (float) HEALING_FACTOR.getAsDouble();
    }

    @Override
    public float BANDAGE_ACC() {
        return (float) BANDAGE_ACC.getAsDouble();
    }

    @Override
    public float BURN_BLEED_RATIO() {
        return (float) BURN_BLEED_RATIO.getAsDouble();
    }

    @Override
    public float INTERNAL_BLEED_RATIO() {
        return (float) INTERNAL_BLEED_RATIO.getAsDouble();
    }

    @Override
    public float OPEN_WOUND_BLEED_RATIO() {
        return (float) OPEN_WOUND_BLEED_RATIO.getAsDouble();
    }

    @Override
    public float INTERNAL_FOOD_HEALING() {
        return (float) INTERNAL_FOOD_HEALING.getAsDouble();
    }

    @Override
    public float BLEED_VOLUME_RATIO() {
        return (float) BLEED_VOLUME_RATIO.getAsDouble();
    }

    @Override
    public float WITHDRAW_RATIO() {
        return (float) WITHDRAW_RATIO.getAsDouble();
    }

    @Override
    public float BASE_SELF_HEALING_TIME() {
        return BASE_SELF_HEALING_TIME.getAsInt();
    }

    @Override
    public float BASE_MED_AVAILABLE_TIME() {
        return BASE_MED_AVAILABLE_TIME.getAsInt();
    }

    @Override
    public float VOLUME_SELF_HEALING_TIME() {
        return VOLUME_SELF_HEALING_TIME.getAsInt();
    }

    @Override
    public float RESISTANCE_CONVERT_RATIO() {
        return (float) RESISTANCE_CONVERT_RATIO.getAsDouble();
    }

    @Override
    public float RESISTANCE_MAX() {
        return (float) RESISTANCE_MAX.getAsDouble();
    }

    @Override
    public float BASE_DISLOCATION_THRESHOLD() {
        return (float) BASE_DISLOCATION_THRESHOLD.getAsDouble();
    }

    @Override
    public float BASE_FRACTURE_THRESHOLD() {
        return (float) BASE_FRACTURE_THRESHOLD.getAsDouble();
    }

    @Override
    public float BASE_DISLOCATION_MAX_PROB() {
        return (float) BASE_DISLOCATION_MAX_PROB.getAsDouble();
    }

    @Override
    public float BASE_FRACTURE_MAX_PROB() {
        return (float) BASE_FRACTURE_MAX_PROB.getAsDouble();
    }

    @Override
    public float FRACTURE_ARTERIAL_PROB() {
        return (float) FRACTURE_ARTERIAL_PROB.getAsDouble();
    }

    @Override
    public float FRACTURE_BLOOD_RATIO() {
        return (float) FRACTURE_BLOOD_RATIO.getAsDouble();
    }

    @Override
    public float BASE_PNEUMOTHORAX_PROB() {
        return (float) BASE_PNEUMOTHORAX_PROB.getAsDouble();
    }

    @Override
    public float BASE_AMPUTATION_THRESHOLD() {
        return (float) BASE_AMPUTATION_THRESHOLD.getAsDouble();
    }

    @Override
    public float BASE_AMPUTATION_MAX_PROB() {
        return (float) BASE_AMPUTATION_MAX_PROB.getAsDouble();
    }

    @Override
    public boolean ALLOW_DOWN() {
        return ALLOW_DOWN.getAsBoolean();
    }

    @Override
    public boolean PLAYER_DOCTOR_HEALING() {
        return PLAYER_DOCTOR_HEALING.getAsBoolean();
    }

    @Override
    public float BYPASS_BRAIN_DAMAGE_PROB() {
        return (float) BYPASS_BRAIN_DAMAGE_PROB.getAsDouble();
    }

    @Override
    public float BYPASS_FOREIGN_PROB() {
        return (float) BYPASS_FOREIGN_PROB.getAsDouble();
    }

    @Override
    public boolean PLAYER_GLOWING() {
        return PLAYER_GLOWING.getAsBoolean();
    }

    @Override
    public boolean PLAYER_DOWN_MOVING() {
        return PLAYER_DOWN_MOVING.getAsBoolean();
    }

    @Override
    public int DAMAGE_PART_STRICK_LEVEL() {
        return DAMAGE_PART_STRICK_LEVEL.getAsInt();
    }

    @Override
    public boolean LIMITED_BODY_PART_VITALITY_LOST() {
        return LIMITED_BODY_PART_VITALITY_LOST.getAsBoolean();
    }

    @Override
    public boolean ENABLE_LIVING_EFFECT() {
        return ENABLE_LIVING_EFFECT.getAsBoolean();
    }

    @Override
    public boolean ENABLE_SELF_SUICIDE() {
        return ENABLE_SELF_SUICIDE.getAsBoolean();
    }

    @Override
    public int SMALL_CONDITION_X() {
        return SMALL_CONDITION_X.getAsInt();
    }

    @Override
    public int SMALL_CONDITION_Y() {
        return SMALL_CONDITION_Y.getAsInt();
    }

    @Override
    public int SMALL_CONDITION_DISAPPEAR_DELAY() {
        return SMALL_CONDITION_DISAPPEAR_DELAY.getAsInt();
    }

    @Override
    public boolean ARMOR_RECALCULATE() {
        return ARMOR_RECALCULATE.getAsBoolean();
    }

    @Override
    public float BLOCK_RECOVER_DELAY() {
        return (float) BLOCK_RECOVER_DELAY.getAsDouble();
    }

    @Override
    public float BLOCK_RECOVER_SPEED() {
        return (float) BLOCK_RECOVER_SPEED.getAsDouble();
    }
}