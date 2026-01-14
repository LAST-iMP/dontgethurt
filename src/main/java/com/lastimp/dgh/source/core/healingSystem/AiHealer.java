package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.api.bodyPart.AbstractExtremities;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.item.medicine.Bandages;
import com.lastimp.dgh.source.register.ModEffects;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public abstract class AiHealer {
    public static final int STEP_TIME = 40;

    public static int doHealing(final LivingEntity source, final LivingEntity target, final HealthCapability h, final int level) {
        Blood blood = (Blood) h.getComponent(BodyComponents.BLOOD);
        Head head = (Head) h.getComponent(BodyComponents.HEAD);
        Torso torso = (Torso) h.getComponent(TORSO);

        BodyComponents component;
        //自动使用起搏器、呼吸气囊
        if (h.oxygenMask().getStackInSlot(0).isEmpty()) ModItems.OXYGEN_MASK.get().heal(source, target);
        if (h.autoPulse().getStackInSlot(0).isEmpty()) ModItems.AUTOPULSE.get().heal(source, target);

        if (level >= 3) {
            if (head.getConditionValue(TRAUMATIC_SHOCK) > 0.15 && !torso.abnormal(ANALGESIA)) {
                //治疗手术休克
                return doUseItem(HEAD, source, target, ModItems.MORPHINE.get(), STEP_TIME);
            }
        }
        if (level >= 5) {
            if (torso.abnormal(AORTIC_RUPTURE)) {
                //治疗主动脉破裂
                if (!torso.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, TORSO, torso);
                else return doUseItem(TORSO, source, target, ModItems.MEDICAL_STENT.get(), STEP_TIME);
            } else if ((component = h.findFor(ARTERIAL_BLEEDING)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
                //治疗动脉出血
                if (!visibleBody.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, visibleBody);
                else return doUseItem(component, source, target, ModItems.SUTURE.get(), STEP_TIME);
            }
        }
        if (level >= 1) {
            if (blood.getConditionValue(BLOOD_LOSS) > 0.4) {
                //治疗失血
                return doUseItem(BLOOD, source, target, ModItems.BLOOD_PACK.get(), STEP_TIME);
            } else if ((component = h.findFor(BANDAGED_DIRTY)) != null && h.getComponent(component) instanceof AbstractVisibleBody) {
                //移除脏绷带
                return doStep(component, source, (comp) -> Bandages.cut(target, comp), Items.SHEARS, STEP_TIME);
            }
        }
        if ((component = h.findFor(BURN)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
            //治疗烧伤
            if (level >= 4) {
                return doUseItem(component, source, target, ModItems.ANTIBIOTIC_GLUE.get(), STEP_TIME);
            } else if (level >= 1 && !visibleBody.abnormal(SURGERY_INCISION)) {
                return doUseItem(component, source, target, ModItems.BANDAGE.get(), STEP_TIME);
            }
        }
        if (level >= 3) {
            if ((component = h.findFor(INTERNAL_INJURY)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
                //治疗内伤
                if (!visibleBody.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, visibleBody);
                else return doUseItem(component, source, target, ModItems.TWEEZER.get(), STEP_TIME);
            }
        }
        if ((component = h.findFor(OPEN_WOUND)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
            //治疗开放伤
            if (level >= 2) {
                return doUseItem(component, source, target, ModItems.SUTURE.get(), STEP_TIME);
            } else if (level >= 1 && !visibleBody.abnormal(SURGERY_INCISION)) {
                return doUseItem(component, source, target, ModItems.BANDAGE.get(), STEP_TIME);
            }
        }
        if ((component = h.findFor(PASS_THROUGH)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
            //治疗贯穿伤
            if (level >= 2) {
                return doUseItem(component, source, target, ModItems.SUTURE.get(), STEP_TIME);
            } else if (level >= 1 && !visibleBody.abnormal(SURGERY_INCISION)) {
                return doUseItem(component, source, target, ModItems.BANDAGE.get(), STEP_TIME);
            }
        }
        if (torso.abnormal(PNEUMOTHORAX)) {
            //治疗气胸
            if (level >= 4) {
                if (!torso.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, TORSO, torso);
                else return doUseItem(TORSO, source, target, ModItems.DRAINAGE.get(), STEP_TIME);
            } else if (level >= 2) {
                return doUseItem(TORSO, source, target, ModItems.NEEDLE.get(), STEP_TIME);
            }
        }
        if (level >= 5) {
            if (torso.abnormal(HEARTRATE_STOP) && !target.hasEffect(ModEffects.ADRENALINE_EFFECT)) {
                //治疗心脏骤停
                return doUseItem(TORSO, source, target, ModItems.ADRENALINE.get(), STEP_TIME);
            }
        }
        if (level >= 4) {
            if ((component = h.findFor(FOREIGN_OBJECT)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
                //治疗体内异物
                if (!visibleBody.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, visibleBody);
                else return doUseItem(component, source, target, ModItems.TWEEZER.get(), STEP_TIME);
            }
        }
        if (level >= 2) {
            if ((component = h.findFor(INFECTION)) != null && h.getComponent(component) instanceof AbstractVisibleBody) {
                //治疗感染
                return doUseItem(component, source, target, ModItems.ANTISEPTIC_SPRAYER.get(), STEP_TIME);
            }
            if (blood.abnormal(SEPSIS) && !blood.abnormal(ANTIBIOTICS)) {
                //治疗败血症
                return doUseItem(BLOOD, source, target, ModItems.ANTIBIOTICS.get(), STEP_TIME);
            }
        }
        if (level > 4) {
            if ((component = h.findFor(BONE_DEATH)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody && visibleBody.boneCrafted() == null) {
                //治疗骨坏死
                if (!visibleBody.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, visibleBody);
                return doBoneReplace(source, target, component, visibleBody);
            }
        }
        if (level > 5) {
            if ((component = h.findFor(TRAUMATIC_AMPUTATION)) != null && h.getComponent(component) instanceof AbstractExtremities extremities) {
                //治疗截肢
                if (!extremities.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, extremities);
                else return doUseItem(component, source, target, ModItems.SURGERY_SAW.get(), STEP_TIME);
            }
        }
        if ((component = h.findForHidden(FRACTURE)) != null && h.getComponent(component) instanceof AbstractVisibleBody visibleBody) {
            if (level >= 3) {
                //治疗骨折
                if (!visibleBody.abnormal(RETRACTED_SKIN)) return doSurgery(source, target, component, visibleBody);
                else return doBoneReplace(source, target, component, visibleBody);
            } else if (level >= 2 && visibleBody instanceof AbstractExtremities extremities) {
                if (!extremities.isBandaged()) return doUseItem(component, source, target, ModItems.BANDAGE.get(), STEP_TIME);
                else return doUseItem(component, source, target, ModItems.GYPSUM.get(), STEP_TIME);
            }
        }
        if ((component = h.findFor(DISLOCATION)) != null && h.getComponent(component) instanceof AbstractExtremities) {
            //治疗脱臼
            return doUseItem(component, source, target, ModItems.WOOD_WRENCH.get(), STEP_TIME);
        }
        if ((component = h.findForHidden(SURGERY_INCISION)) != null && h.getComponent(component) instanceof AbstractVisibleBody) {
            return doUseItem(component, source, target, ModItems.SUTURE.get(), STEP_TIME);
        }

        return 0;
    }

    private static int doStep(BodyComponents component, LivingEntity source, Consumer<BodyComponents> func, Item item, int time) {
        func.accept(component);
        source.setItemInHand(InteractionHand.MAIN_HAND, item.getDefaultInstance());
        return time;
    }

    private static int doUseItem(BodyComponents component, LivingEntity source, LivingEntity target, AbstractDirectHealItems item, int time) {
        return doStep(component, source, (comp) -> item.heal(source, target), item, time);
    }

    private static int doUseItem(BodyComponents component, LivingEntity source, LivingEntity target, AbstractPartlyHealItem item, int time) {
        return doStep(component, source, (comp) -> item.heal(source, target, comp), item, time);
    }

    private static int doSurgery(LivingEntity source, LivingEntity target, BodyComponents component, AbstractVisibleBody body) {
        if (!body.abnormal(SURGERY_INCISION)) {
            doUseItem(component, source, target, ModItems.SCALPEL.get(), STEP_TIME);
        } else if (!body.abnormal(CLAMPED_BLEEDING)) {
            doUseItem(component, source, target, ModItems.HEMOSTAT.get(), STEP_TIME);
        } else if (!body.abnormal(RETRACTED_SKIN)) {
            doUseItem(component, source, target, ModItems.RETRACTOR.get(), STEP_TIME);
        }
        return STEP_TIME;
    }

    private static int doBoneReplace(LivingEntity source, LivingEntity target, BodyComponents component, AbstractVisibleBody body) {
        if (!body.abnormal(DRILLED_BONES)) {
            doUseItem(component, source, target, ModItems.SURGICAL_DRILL.get(), STEP_TIME);
        } else if (body.boneCrafted() == null) {
            doUseItem(component, source, target, ModItems.BONE_IMPLANTS.get(), STEP_TIME);
        }
        return STEP_TIME;
    }
}