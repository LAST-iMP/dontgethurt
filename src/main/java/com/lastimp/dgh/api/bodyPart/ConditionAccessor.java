package com.lastimp.dgh.api.bodyPart;

import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.bodyPart.base.*;
import com.lastimp.dgh.source.item.medicine.Sutures;
import com.lastimp.dgh.source.item.tool.Scalpel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;
import java.util.function.Function;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;

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
    public static final Map<ResourceLocation, RegistryObject<Item>> bones = new HashMap<>();

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

        Sutures.addCoverOnHeal(SAWED_BONES);
        Scalpel.addDiscoverOnHeal(SAWED_BONES);
        for (var key : bones.keySet()) {
            Sutures.addCoverOnHeal(key);
            Scalpel.addDiscoverOnHeal(key);
        }
    }
}
