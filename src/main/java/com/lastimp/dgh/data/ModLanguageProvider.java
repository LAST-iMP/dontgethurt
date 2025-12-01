
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.dgh", "dgh");

        for (BodyCondition condition : BodyCondition.values()) {
            this.add(condition.name(), condition.translation);
        }

        this.add("gui.dgh.health_gui.title", "健康状态");
        this.add("key.dgh.health_menu", "健康面板");
        this.add("key.category.dgh", "DGH");
        this.add("dgh.book.medical_guide", "医疗指南");
        this.add("dgh.landing_text", "未经过审批，需要谨慎使用。");
        this.add(ModItems.HEALTH_SCANNER.get(), "健康扫描仪");
        this.add(ModItems.BLOOD_SCANNER.get(), "血液扫描仪");
        this.add(ModItems.BLOOD_PACK.get(), "血袋");
        this.add(ModItems.BLOOD_PACK_EMPTY.get(), "空血袋");
        this.add(ModItems.BANDAGE.get(), "绷带");
        this.add(ModItems.MORPHINE.get(), "吗啡");
        this.add(ModItems.GYPSUM.get(), "石膏");
        this.add(ModItems.SUTURE.get(), "缝合线");
//        this.add(ModItems.OPERATING_BED_BLOCK_ITEM.get(), "手术床");
        this.add(ModItems.HEALTH_CARE_BAG.get(), "医疗包");
        this.add(ModItems.SURGERY_TOOL_BAG.get(), "手术工具包");
        this.add(ModItems.WOOD_WRENCH.get(), "木扳手");
        this.add(ModItems.SCALPEL.get(), "手术刀");
        this.add(ModItems.HEMOSTAT.get(), "止血钳");
        this.add(ModItems.RETRACTOR.get(), "牵开器");
        this.add(ModItems.SURGICAL_DRILL.get(), "手术钻");
        this.add(ModItems.TWEEZER.get(), "镊子");
        this.add(ModItems.BONE_IMPLANTS.get(), "骨骼植入物");

        this.add(ModEffects.STAGGER_EFFECT.get(), "缓行");
        this.add(ModEffects.INTENSE_PAIN_EFFECT.get(), "剧痛");
        this.add(ModEffects.SWEATING_EFFECT.get(), "出汗");
        this.add(ModEffects.CRAVING_EFFECT.get(), "渴望");
        this.add(ModEffects.KEEP_LIVING_EFFECT.get(), "长生久视");
        this.add(ModEffects.CURE_EFFECT.get(), "重振旗鼓");
        this.add(ModEffects.PALE_SKIN.get(), "皮肤苍白");
        this.add(ModEffects.HARD_BREATH.get(), "呼吸困难");
//        this.add(ModEffects.INCREASED_HEARTRATE.get(), "心跳加快");
    }
}
