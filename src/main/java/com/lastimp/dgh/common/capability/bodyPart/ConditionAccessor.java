package com.lastimp.dgh.common.capability.bodyPart;

import com.lastimp.dgh.common.capability.bodyPart.bodies.Blood;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Head;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractExtremities;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractVisibleBody;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.item.medicine.Sutures;
import com.lastimp.dgh.common.item.tool.Scalpel;
import com.lastimp.dgh.common.utils.Lazy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.function.Function;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.*;

public class ConditionAccessor {
    public static final Map<ResourceLocation, Lazy<BodyCondition>> conditions = new HashMap<>();
    public static final List<ResourceLocation> bloodConditions = new LinkedList<>();
    public static final List<ResourceLocation> injuryConditions = new LinkedList<>();
    public static final List<ResourceLocation> surgeryConditions = new LinkedList<>();
    public static final List<ResourceLocation> painConditions = new LinkedList<>();
    public static final List<ResourceLocation> comfortConditions = new LinkedList<>();
    public static final List<ResourceLocation> resistConditions = new LinkedList<>();
    public static final Set<ResourceLocation> eyeVisible = new HashSet<>();
    public static final Set<ResourceLocation> selfHealing = new HashSet<>();
    public static final Map<ResourceLocation, IEntry<Item>> bones = new HashMap<>();

    public static ResourceLocation addCondition(ResourceLocation name, Function<ResourceLocation, BodyCondition> func) {
        conditions.put(name, Lazy.of(() -> func.apply(name)));
        return name;
    }

    public static BodyCondition get(ResourceLocation location) {
        return conditions.get(location).get();
    }

    public static void init() {
        AbstractVisibleBody.addCondition(List.of(
                SURGERY_INCISION,
                CLAMPED_BLEEDING,
                RETRACTED_SKIN,
                DRILLED_BONES,
                SAWED_BONES,

                ARTERIAL_BLEEDING,

                BURN,
                INTERNAL_INJURY,
                OPEN_WOUND,
                PASS_THROUGH,
                BLEED,
                INFECTION,
                FOREIGN_OBJECT,
                BANDAGED,
                HERB_BANDAGED,
                BANDAGED_DIRTY,
                OINTMENT,
                CLAMPED_ARTERIES,

                FRACTURE,
                INTENSE_PAIN,
                PLASTER_CAST,
                CLAMP_PLATE,
                BONE_DAMAGE,
                BONE_DEATH,

                BONE_WOOD,
                BONE_STONE,
                BONE_COPPER,
                BONE_IRON,
                BONE_GOLD,
                BONE_DIMOND,
                BONE_NETHERITE,

                BURN_RES,
                INTERNAL_RES,
                OPEN_WOUND_RES
        ));
        AbstractExtremities.addCondition(List.of(
                DISLOCATION,
                GANGRENE,
                SURGICAL_AMPUTATION,
                TRAUMATIC_AMPUTATION
        ));
        Head.addCondition(List.of(
                WITHDRAW,
                TRAUMATIC_SHOCK,
                BRAIN_DAMAGE,
                COMA
        ));
        Blood.addCondition(List.of(
                SEPSIS,
                HEMOTRANSFUSION,
                BLOOD_LOSS,
                BLOOD_PRESSURE,
                PH_LEVEL,
                IMMUNITY,

                OPIATE_OVERDOSE,
                OPIATE_ADDICTED,
                OXYGEN,
                ANTIBIOTICS,
                HARDENER
        ));
        Torso.addCondition(List.of(
                ANALGESIA,
                RESPIRATORY_ARREST,
                AORTIC_RUPTURE,
                HEARTRATE_INCREASE,
                HEARTRATE_IRREGULAR,
                HEARTRATE_STOP,
                PNEUMOTHORAX,

                PNEUMOTHORAX_NEEDLE
        ));

        conditions.values().forEach(Lazy::get);

        Sutures.addCoverOnHeal(SAWED_BONES);
        Scalpel.addDiscoverOnHeal(SAWED_BONES);
        for (var key : bones.keySet()) {
            Sutures.addCoverOnHeal(key);
            Scalpel.addDiscoverOnHeal(key);
        }
    }
}
