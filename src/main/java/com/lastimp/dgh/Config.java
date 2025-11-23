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

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder()
            .comment("General settings")
            .push("general_1.1.0");

    public static final ModConfigSpec.DoubleValue DIRTY_BANDAGE_RATIO = BUILDER
            .defineInRange("DIRTY_BANDAGE_RATIO",0.05,0,1);

    public static final ModConfigSpec.DoubleValue BANDAGE_ACC = BUILDER
            .defineInRange("BANDAGE_ACC",2.0,0,10);

    public static final ModConfigSpec.DoubleValue BURN_BLEED_RATIO = BUILDER
            .defineInRange("BURN_BLEED_RATIO",0.6,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_BLEED_RATIO = BUILDER
            .defineInRange("INTERNAL_BLEED_RATIO",0.4,0,10);

    public static final ModConfigSpec.DoubleValue OPEN_WOUND_BLEED_RATIO = BUILDER
            .defineInRange("OPEN_WOUND_BLEED_RATIO",0.8,0,10);

    public static final ModConfigSpec.DoubleValue INTERNAL_FOOD_HEALING = BUILDER
            .defineInRange("INTERNAL_FOOD_HEALING",4.0,1.0, Float.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLEED_VOLUME_RATIO = BUILDER
            .defineInRange("BLEED_VOLUME_RATIO",0.03,0, Float.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_SELF_HEALING_TIME = BUILDER
            .defineInRange("BASE_SELF_HEALING_TIME",500,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BASE_MED_AVAILABLE_TIME = BUILDER
            .defineInRange("BASE_MED_AVAILABLE_TIME",100,1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue VOLUME_SELF_HEALING_TIME = BUILDER
            .defineInRange("VOLUME_SELF_HEALING_TIME",50,1, Integer.MAX_VALUE);

    // 检验item名称是否合法
    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.withDefaultNamespace(itemName));
    }

    // 构建配置
    public static final ModConfigSpec SPEC = BUILDER.pop().build();

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
        dirty_bandage_ratio = (float) DIRTY_BANDAGE_RATIO.getAsDouble();
        bandage_acc = (float) BANDAGE_ACC.getAsDouble();
        burn_bleed_ratio = (float) BURN_BLEED_RATIO.getAsDouble();
        internal_bleed_ratio = (float) INTERNAL_BLEED_RATIO.getAsDouble();
        open_wound_bleed_ratio = (float) OPEN_WOUND_BLEED_RATIO.getAsDouble();
        internal_food_healing = (float) INTERNAL_FOOD_HEALING.getAsDouble();
        bleed_volume_ratio = (float) BLEED_VOLUME_RATIO.getAsDouble();

        base_self_healing_time = BASE_SELF_HEALING_TIME.getAsInt();
        base_med_available_time = BASE_MED_AVAILABLE_TIME.getAsInt();
        volume_self_healing_time = VOLUME_SELF_HEALING_TIME.getAsInt();
    }

}