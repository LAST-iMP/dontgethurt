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

package com.lastimp.dgh.api.enums;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.lastimp.dgh.DontGetHurt.EPS;

public enum BodyCondition {

    //肢体
    BURN            ("烧伤", 1.0f / Config.base_self_healing_time, 0.2f, BodyCondition.pathRoot+"burn.png"),
    INTERNAL_INJURY ("内伤", 1.0f / Config.base_self_healing_time, 1.0f, BodyCondition.pathRoot+"internal_injury.png"),
    OPEN_WOUND      ("开放伤", 1.0f / Config.base_self_healing_time, 0.5f, BodyCondition.pathRoot+"open_wound.png"),
    BLEED           ("出血", 0.0f, 0.0f, BodyCondition.pathRoot+"bleeding.png"),
    INFECTION       ("感染", 0.0f, 0.0f),
    FOREIGN_OBJECT  ("体内异物", 0.0f, 0.0f),

    BANDAGED        ("绷带包扎", 1.0f / Config.base_med_available_time / 2, 0.75f, 0xFF00FF00, BodyCondition.pathRoot+"bandage.png"),
    BANDAGED_DIRTY  ("脏绷带", 0.0f, 1.0f, BodyCondition.pathRoot+"bandage_dirty.png"),
    OINMENTED       ("药膏涂抹", 0.0f, 0.0f),
    //四肢
    DISLOCATION     ("脱臼", 0.0f, 1.0f, 0xFFFFFF00, BodyCondition.pathRoot+"dislocation.png"),
    FRACTURE        ("骨折", 0.0f, 1.0f, BodyCondition.pathRoot+"fracture.png"),
    INTENSE_PAIN    ("剧痛", 0.0f, 1.0f, 0xFFFFFF00, BodyCondition.pathRoot+"intense_pain.png"),
    PLASTER_CAST    ("石膏固定", 0.0f, 1.0f, 0xFF00FF00, BodyCondition.pathRoot+"plaster_cast.png"),
    //躯干
    ANALGESIA       ("镇痛", 1.0f/ Config.base_med_available_time, 1.0f, 0xFF00FF00, BodyCondition.pathRoot+"analgesia.png"),
    //头脑
    WITHDRAW        ("戒断", 1.0f/ Config.base_med_available_time, 1.0f, BodyCondition.pathRoot+"withdraw.png"),
    //血液
    SEPSIS          ("败血症", 0.0f, 0.0f),
    HEMOTRANSFUSION ("输血性休克", 0.0f, 0.0f),
    BLOOD_LOSS      ("失血", 1.0f / Config.volume_self_healing_time, 0.0f),
    BLOOD_PRESSURE  ("血压", 0.0f, 0.0f),
    PH_LEVEL        ("酸碱性", 0.0f, 0.0f),
    IMMUNITY        ("免疫力", 0.0f, 0.0f),
    OPIATE_OVERDOSE ("阿片中毒", 1.0f / Config.base_self_healing_time, 1.0f),
    OPIATE_ADDICTED ("阿片成瘾", 1.0f / (Config.base_self_healing_time * 5), 1.0f)
    ;

    private static final String pathRoot = "textures/gui/sprites/container/condition_icons/";
    public final String translation;
    public final float defaultValue;
    public final float minValue;
    public final float maxValue;

    public final float healingSpeed;
    public final float healingTS;

    public final ResourceLocation texture;
    public final int color;

    BodyCondition(String translation, float healingSpeed, float healingTS) {
        this(translation, healingSpeed, healingTS,null);
    }

    BodyCondition(String translation, float healingSpeed, float healingTS, String path) {
        this(translation, healingSpeed, healingTS, 0.0f, 0.0f, 1.0f, path);
    }

    BodyCondition(String translation, float healingSpeed, float healingTS, int color, String path) {
        this(translation, healingSpeed, healingTS, 0.0f, 0.0f, 1.0f, color, path);
    }

    BodyCondition(String translation, float healingSpeed, float healingTS, float defaultValue, float minValue, float maxValue, String path) {
        this(translation, healingSpeed, healingTS, defaultValue, minValue, maxValue, 0xFFFF0000, path);
    }

    BodyCondition(String translation, float healingSpeed, float healingTS, float defaultValue, float minValue, float maxValue, int color, String path) {
        this.translation = translation;
        this.healingSpeed = healingSpeed;
        this.healingTS = healingTS;
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.color = color;
        this.texture = path == null ? null : ResourceLocation.fromNamespaceAndPath(DontGetHurt.MODID, path);
    }

    public boolean abnormal(float value) {
        return defaultValue < value - EPS || defaultValue > value + EPS;
    }

    public boolean isInjury() {
        return this == BURN || this == INTERNAL_INJURY || this == OPEN_WOUND || this == BLEED ||
                this == INFECTION || this == FOREIGN_OBJECT || this == BANDAGED_DIRTY;
    }

    @Override
    public String toString() {
        return getComponent().getString();
    }

    public Component getComponent() {
        return Component.translatable(this.name());
    }

    public static BodyCondition[] allConditions() {
        return values();
    }
}


