
package com.lastimp.dgh;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.1.3");

    public static final ForgeConfigSpec.DoubleValue BODY_LIFE_FACTOR = BUILDER
            .defineInRange("BODY_LIFE_FACTOR", 1.0f, 0, 1000);

    public static final ForgeConfigSpec.DoubleValue DIRTY_BANDAGE_RATIO = BUILDER
            .defineInRange("DIRTY_BANDAGE_RATIO",0.05,0,1);

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
            .defineInRange("BLEED_VOLUME_RATIO",0.03,0, Float.MAX_VALUE);

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

    // 构建配置
    public static final ForgeConfigSpec SPEC = BUILDER.pop().build();

    public static float body_life_factor;
    public static float dirty_bandage_ratio;
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

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        body_life_factor = (float) (double)BODY_LIFE_FACTOR.get();
        dirty_bandage_ratio = (float) (double) DIRTY_BANDAGE_RATIO.get();
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
    }

}