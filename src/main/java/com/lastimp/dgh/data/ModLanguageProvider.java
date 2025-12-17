
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static com.lastimp.dgh.api.tags.ModDamageType.*;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.add("itemGroup.dgh", "dgh");


        this.add(BodyCondition.BURN.toString(), "烧伤");
        this.add(BodyCondition.INTERNAL_INJURY.toString(), "内伤");
        this.add(BodyCondition.OPEN_WOUND.toString(), "开放伤");
        this.add(BodyCondition.BLEED.toString(), "出血");
        this.add(BodyCondition.INFECTION.toString(), "感染");
        this.add(BodyCondition.FOREIGN_OBJECT.toString(), "体内异物");
        this.add(BodyCondition.FRACTURE.toString(), "骨折");
        this.add(BodyCondition.INTENSE_PAIN.toString(), "剧痛");

        this.add(BodyCondition.BANDAGED.toString(), "绷带包扎");
        this.add(BodyCondition.BANDAGED_DIRTY.toString(), "脏绷带");
        this.add(BodyCondition.OINTMENT.toString(), "药膏涂抹");

        this.add(BodyCondition.BURN_RES.toString(), "烧伤抗性");
        this.add(BodyCondition.INTERNAL_RES.toString(), "内伤抗性");
        this.add(BodyCondition.OPEN_WOUND_RES.toString(), "外伤抗性");

        this.add(BodyCondition.SURGERY_INCISION.toString(), "手术切口");
        this.add(BodyCondition.CLAMPED_BLEEDING.toString(), "夹闭止血");
        this.add(BodyCondition.RETRACTED_SKIN.toString(), "皮肤牵开");
        this.add(BodyCondition.DRILLED_BONES.toString(), "骨骼钻孔");
        this.add(BodyCondition.SAWED_BONES.toString(), "骨锯开");

        this.add(BodyCondition.DISLOCATION.toString(), "脱臼");
        this.add(BodyCondition.PLASTER_CAST.toString(), "石膏固定");
        this.add(BodyCondition.ARTERIAL_BLEEDING.toString(), "动脉出血");
        this.add(BodyCondition.CLAMPED_ARTERIES.toString(), "动脉夹闭");
        this.add(BodyCondition.GANGRENE.toString(), "坏疽");

        this.add(BodyCondition.ANALGESIA.toString(), "镇痛");
        this.add(BodyCondition.RESPIRATORY_ARREST.toString(), "呼吸停止");
        this.add(BodyCondition.AORTIC_RUPTURE.toString(), "主动脉破裂");

        this.add(BodyCondition.WITHDRAW.toString(), "戒断");
        this.add(BodyCondition.TRAUMATIC_SHOCK.toString(), "手术休克");
        this.add(BodyCondition.BRAIN_DAMAGE.toString(), "脑损伤");

        this.add(BodyCondition.SEPSIS.toString(), "败血症");
        this.add(BodyCondition.HEMOTRANSFUSION.toString(), "输血性休克");
        this.add(BodyCondition.BLOOD_LOSS.toString(), "失血");
        this.add(BodyCondition.BLOOD_PRESSURE.toString(), "血压");
        this.add(BodyCondition.PH_LEVEL.toString(), "酸碱性");
        this.add(BodyCondition.IMMUNITY.toString(), "免疫力");
        this.add(BodyCondition.OPIATE_OVERDOSE.toString(), "阿片中毒");
        this.add(BodyCondition.OPIATE_ADDICTED.toString(), "阿片成瘾");
        this.add(BodyCondition.OXYGEN.toString(), "低血氧");
        this.add(BodyCondition.BONE_WOOD.toString(), "木骨植入");
        this.add(BodyCondition.BONE_STONE.toString(), "石骨植入");
        this.add(BodyCondition.BONE_COPPER.toString(), "铜骨植入");
        this.add(BodyCondition.BONE_IRON.toString(), "铁骨植入");
        this.add(BodyCondition.BONE_GOLD.toString(), "金骨植入");
        this.add(BodyCondition.BONE_DIMOND.toString(), "钻骨植入");
        this.add(BodyCondition.BONE_NETHERITE.toString(), "下界骨植入");


        this.add("gui.dgh.health_gui.title", "健康状态");
        this.add(KeyBinding.KEY_CATEGORY_DGH, "DGH");
        this.add(KeyBinding.KEY_HEALTH_MENU, "健康面板");
        this.add(KeyBinding.KEY_GIVE_UP, "放弃生命");
        this.add(KeyBinding.KEY_CALL_FOR_HELP, "呼叫救援");
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
        this.add(ModItems.HEALTH_CARE_BAG.get(), "医疗包");
        this.add(ModItems.SURGERY_TOOL_BAG.get(), "手术工具包");
        this.add(ModItems.WOOD_WRENCH.get(), "木扳手");
        this.add(ModItems.SCALPEL.get(), "手术刀");
        this.add(ModItems.HEMOSTAT.get(), "止血钳");
        this.add(ModItems.RETRACTOR.get(), "牵开器");
        this.add(ModItems.SURGICAL_DRILL.get(), "手术钻");
        this.add(ModItems.TWEEZER.get(), "镊子");
        this.add(ModItems.BONE_IMPLANTS.get(), "骨骼植入物");
        this.add(ModItems.BONE_IMPLANTS_WOOD.get(), "木骨植入物");
        this.add(ModItems.BONE_IMPLANTS_STONE.get(), "石骨植入物");
        this.add(ModItems.BONE_IMPLANTS_COPPER.get(), "铜骨植入物");
        this.add(ModItems.BONE_IMPLANTS_IRON.get(), "铁骨植入物");
        this.add(ModItems.BONE_IMPLANTS_GOLD.get(), "金骨植入物");
        this.add(ModItems.BONE_IMPLANTS_DIMOND.get(), "钻骨植入物");
        this.add(ModItems.BONE_IMPLANTS_NETHERITE.get(), " ");
        this.add(ModItems.SURGERY_SAW.get(), "手术锯");
        this.add(ModItems.BONE_NATURAL.get(), "自然骨");
        this.add(ModItems.BONE_WOOD.get(), "木骨");
        this.add(ModItems.BONE_STONE.get(), "石骨");
        this.add(ModItems.BONE_COPPER.get(), "铜骨");
        this.add(ModItems.BONE_IRON.get(), "铁骨");
        this.add(ModItems.BONE_GOLD.get(), "金骨");
        this.add(ModItems.BONE_DIMOND.get(), "钻骨");
        this.add(ModItems.BONE_NETHERITE.get(), "下界合金骨");
        this.add(ModItems.NALOXONE.get(), "烯丙羟吗啡酮");
        this.add(ModItems.MEDICAL_STENT.get(), "医用支架");
        this.add(ModItems.TOURNIQUET.get(), "动脉止血带");

        this.add(ModEffects.STAGGER_EFFECT.get(), "缓行");
        this.add(ModEffects.INTENSE_PAIN_EFFECT.get(), "剧痛");
        this.add(ModEffects.SWEATING_EFFECT.get(), "出汗");
        this.add(ModEffects.CRAVING_EFFECT.get(), "渴望");
        this.add(ModEffects.KEEP_LIVING_EFFECT.get(), "长生久视");
        this.add(ModEffects.CURE_EFFECT.get(), "重振旗鼓");
        this.add(ModEffects.PALE_SKIN.get(), "皮肤苍白");
        this.add(ModEffects.HARD_BREATH.get(), "呼吸困难");
//        this.add(ModEffects.INCREASED_HEARTRATE.get(), "心跳加快");

        this.add("death.attack."+OPEN_WOUND_DAMAGE.location(), "%1$s 的身体被撕碎了");
        this.add("death.attack."+INTERNAL_INJURY_DAMAGE.location(), "%1$s 体内一塌糊涂");
        this.add("death.attack."+BURN_DAMAGE.location(), "%1$s 变成了黑碳");
        this.add("death.attack."+BRAIN_DAMAGE.location(), "%1$s 变成了植物人");
        this.add("death.attack."+BLEED_DAMAGE.location(), "%1$s 失血过多");
        this.add("death.attack."+SURGERY_DAMAGE.location(), "%1$s 死于手术事故");
        this.add("death.attack."+CANT_BREATH_DAMAGE.location(), "%1$s 无法呼吸");

        this.add("death.attack."+OPEN_WOUND_DAMAGE.location()+".player", "%1$s 的身体被撕碎了");
        this.add("death.attack."+INTERNAL_INJURY_DAMAGE.location()+".player", "%1$s 体内一塌糊涂");
        this.add("death.attack."+BURN_DAMAGE.location()+".player", "%1$s 变成了黑碳");
        this.add("death.attack."+BRAIN_DAMAGE.location()+".player", "%1$s 变成了植物人");
        this.add("death.attack."+BLEED_DAMAGE.location()+".player", "%1$s 失血过多");
        this.add("death.attack."+SURGERY_DAMAGE.location()+".player", "%1$s 死于手术事故");
        this.add("death.attack."+CANT_BREATH_DAMAGE.location()+".player", "%1$s 无法呼吸");
    }
}
