
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.Register.ModEffects;
import com.lastimp.dgh.source.Register.ModItems;
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
        this.add(ModItems.OPERATING_BED_BLOCK_ITEM.get(), "手术床");

        this.add(ModEffects.STAGGER_EFFECT.get(), "缓行");
        this.add(ModEffects.INTENSE_PAIN_EFFECT.get(), "剧痛");
        this.add(ModEffects.SWEATING_EFFECT.get(), "出汗");
        this.add(ModEffects.CRAVING_EFFECT.get(), "渴望");

    }
}
