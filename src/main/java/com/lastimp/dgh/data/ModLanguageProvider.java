
package com.lastimp.dgh.data;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.client.hotkey.KeyBinding;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.register.ModPotions;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import static com.lastimp.dgh.api.tags.ModDamageType.*;
import static com.lastimp.dgh.api.tags.ModDamageType.BLEED_DAMAGE;
import static com.lastimp.dgh.api.tags.ModDamageType.BRAIN_DAMAGE;
import static com.lastimp.dgh.api.tags.ModDamageType.CANT_BREATH_DAMAGE;
import static com.lastimp.dgh.api.tags.ModDamageType.SURGERY_DAMAGE;

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

        this.add(BodyCondition.BONE_DAMAGE.toString(), "骨损伤");
        this.add(BodyCondition.BONE_DEATH.toString(), "骨坏死");

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
        this.add(BodyCondition.SURGICAL_AMPUTATION.toString(), "手术性截肢");
        this.add(BodyCondition.TRAUMATIC_AMPUTATION.toString(), "创伤性截肢");

        this.add(BodyCondition.ANALGESIA.toString(), "镇痛");
        this.add(BodyCondition.RESPIRATORY_ARREST.toString(), "呼吸停止");
        this.add(BodyCondition.AORTIC_RUPTURE.toString(), "主动脉破裂");
        this.add(BodyCondition.HEARTRATE_INCREASE.toString(), "心率加快");
        this.add(BodyCondition.HEARTRATE_IRREGULAR.toString(), "心律不齐");
        this.add(BodyCondition.HEARTRATE_STOP.toString(), "心跳停止");
        this.add(BodyCondition.PNEUMOTHORAX.toString(), "气胸");
        this.add(BodyCondition.PNEUMOTHORAX_NEEDLE.toString(), "气胸针");

        this.add(BodyCondition.WITHDRAW.toString(), "戒断");
        this.add(BodyCondition.TRAUMATIC_SHOCK.toString(), "手术休克");
        this.add(BodyCondition.BRAIN_DAMAGE.toString(), "脑损伤");
        this.add(BodyCondition.COMA.toString(), "昏迷");

        this.add(BodyCondition.SEPSIS.toString(), "败血症%s");
        this.add(BodyCondition.HEMOTRANSFUSION.toString(), "输血性休克%s");
        this.add(BodyCondition.BLOOD_LOSS.toString(), "失血%s");
        this.add(BodyCondition.BLOOD_PRESSURE.toString(), "血压%s");
        this.add(BodyCondition.PH_LEVEL.toString(), "酸碱性%s");
        this.add(BodyCondition.IMMUNITY.toString(), "免疫力%s");
        this.add(BodyCondition.OPIATE_OVERDOSE.toString(), "阿片中毒%s");
        this.add(BodyCondition.OPIATE_ADDICTED.toString(), "阿片成瘾%s");
        this.add(BodyCondition.OXYGEN.toString(), "低血氧%s");
        this.add(BodyCondition.ANTIBIOTICS.toString(), "广谱抗生素%s");

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
        this.add(ModItems.BONE_IMPLANTS_NETHERITE.get(), "下界合金骨植入物");
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
        this.add(ModItems.NEEDLE.get(), "气胸针");
        this.add(ModItems.DRAINAGE.get(), "引流管");
        this.add(ModItems.ADRENALINE.get(), "肾上腺素");
        this.add(ModItems.OXYGEN_MASK.get(), "急救呼吸气囊");
        this.add(ModItems.ANTIBIOTIC_OINTMENT.get(), "抗生素软膏");
        this.add(ModItems.ANTISEPTIC_SPRAYER.get(), "消毒喷雾器");
        this.add(ModItems.ANTISEPTIC.get(), "消毒剂");
        this.add(ModItems.AUTOPULSE.get(), "自动心肺复苏器");
        this.add(ModItems.ANTIBIOTICS.get(), "广谱抗生素");
        this.add(ModItems.LIMB_REF_BEG.get(), "器官储存箱");
        this.add(ModItems.HUMAN_HAND.get(), "手臂");
        this.add(ModItems.HUMAN_LEG.get(), "腿");
        this.add(ModItems.PLASTIC_SKIN.get(), "可塑人造皮肤");
        this.add(ModItems.ANTIBIOTIC_GLUE.get(), "抗生素凝胶");
        this.add(ModItems.STASIS_BAG.get(), "停滞袋");


        this.add(ModEffects.STAGGER_EFFECT.get(), "缓行");
        this.add(ModEffects.INTENSE_PAIN_EFFECT.get(), "剧痛");
        this.add(ModEffects.SWEATING_EFFECT.get(), "出汗");
        this.add(ModEffects.CRAVING_EFFECT.get(), "渴望");
        this.add(ModEffects.KEEP_LIVING_EFFECT.get(), "长生久视");
        this.add(ModEffects.CURE_EFFECT.get(), "重振旗鼓");
        this.add(ModEffects.PALE_SKIN_EFFECT.get(), "皮肤苍白");
        this.add(ModEffects.HARD_BREATH_EFFECT.get(), "呼吸困难");
        this.add(ModEffects.INCREASED_HEARTRATE_EFFECT.get(), "心跳加快");
        this.add(ModEffects.INFLAMMATION_EFFECT.get(), "炎症");
        this.add(ModEffects.FEVER_EFFECT.get(), "发烧");
        this.add(ModEffects.ADRENALINE_EFFECT.get(), "肾上腺素");
        this.add(ModEffects.COMBAT_STIMULANT_EFFECT.get(), "战斗兴奋剂");

        this.addPotion(ModPotions.COMBAT_STIMULANT_POTION, "战斗兴奋剂");

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

    private void addPotion(RegistryObject<Potion> potion, String translation) {
        String potionName = ModPotions.POTIONS.get(potion);
        // 普通药水
        this.add("item.minecraft.potion.effect." + potionName, translation);
        // 喷溅药水
        this.add("item.minecraft.splash_potion.effect." + potionName, "喷溅型" + translation);
        // 滞留药水
        this.add("item.minecraft.lingering_potion.effect." + potionName, "滞留型" + translation);
        // 药箭（如果你允许合成）
        this.add("item.minecraft.tipped_arrow.effect." + potionName, translation + "之箭");
    }
}
