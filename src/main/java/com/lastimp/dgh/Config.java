/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.1.0");

    public static final ForgeConfigSpec.DoubleValue DIRTY_BANDAGE_RATIO = BUILDER
            .defineInRange("DIRTY_BANDAGE_RATIO",0.05,0,1);

    public static final ForgeConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ForgeConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .defineInRange("BURN_BLEED_RATIO",0.6,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .defineInRange("INTERNAL_BLEED_RATIO",0.4,0,10);

    public static final ForgeConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ForgeConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .defineInRange("BLEED_VOLUME_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .defineInRange("VOLUME_SELF_HEALING_TIME",50,1, Integer.MAX_VALUE);

    // 检验item名称是否合法
    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    // 构建配置
    public static final ForgeConfigSpec SPEC = BUILDER.pop().build();

    public static float dirty_bandage_ratio;
    public static float bandage_acc;
    public static float burn_bleed_ratio;
    public static float internal_bleed_ratio;
    public static float open_wound_bleed_ratio;
    public static float internal_food_healing;
    public static float bleed_volume_ratio;

    public static int base_self_healing_time;
    public static int base_med_available_time;
    public static int volume_self_healing_time;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        dirty_bandage_ratio = (float) (double) DIRTY_BANDAGE_RATIO.get();
        bandage_acc = (float) (double)BANDAGE_ACC.get();
        burn_bleed_ratio = (float) (double) BURN_BLEED_RATIO.get();
        internal_bleed_ratio = (float) (double) INTERNAL_BLEED_RATIO.get();
        open_wound_bleed_ratio = (float) (double) OPEN_WOUND_BLEED_RATIO.get();
        internal_food_healing = (float) (double) INTERNAL_FOOD_HEALING.get();
        bleed_volume_ratio = (float) (double) BLEED_VOLUME_RATIO.get();

        base_self_healing_time = BASE_SELF_HEALING_TIME.get();
        base_med_available_time = BASE_MED_AVAILABLE_TIME.get();
        volume_self_healing_time = VOLUME_SELF_HEALING_TIME.get();
    }

}