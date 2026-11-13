package com.lastimp.dgh.common.capability.bodyPart;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lastimp.dgh.api.DGH_API;
import com.lastimp.dgh.api.ComponentLevel;
import com.lastimp.dgh.common.capability.bodyPart.base.*;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Blood;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Head;
import com.lastimp.dgh.common.capability.bodyPart.bodies.Torso;
import com.lastimp.dgh.common.item.medicine.Sutures;
import com.lastimp.dgh.common.item.tool.Scalpel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

import static com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition.SAWED_BONES;

public class ConditionAccessor {
    private static Phase phase = Phase.REGISTERING;
    private static Map<ResourceLocation, BodyCondition> conditions = new HashMap<>();
    private static List<ResourceLocation> bloodConditions = new LinkedList<>();
    private static List<ResourceLocation> bodyConditions = new LinkedList<>();
    private static List<ResourceLocation> resistConditions = new LinkedList<>();

    public static void init() {
        BodyCondition.registerModCondition();

        if (phase() != ConditionAccessor.Phase.REGISTERING) throw new RuntimeException("cannot build dgh body condition after initialized");
        phase = Phase.BUILDING;

        conditionInit();
        handlerInit();

        phase = Phase.FROZEN;

        Sutures.addCoverOnHeal(SAWED_BONES);
        Scalpel.addDiscoverOnHeal(SAWED_BONES);
    }

    private static void conditionInit() {
        DGH_API.conditionAPI.conditions().forEach((name, builder) -> {
            var instance = builder.build();
            conditions.put(name, instance);

            if (builder.components == ComponentLevel.BLOOD) {
                bloodConditions.add(name);
            } else {
                bodyConditions.add(name);
            }

            if (instance.isResist()) {
                resistConditions.add(name);
            }

            switch (builder.components) {
                case VISIBLE_BODY: AbstractVisibleBody.addCondition(name); break;
                case EXTREMITIES: AbstractExtremities.addCondition(name); break;
                case ARM: AbstractArm.addCondition(name); break;
                case LEG: AbstractLeg.addCondition(name); break;
                case TORSO: Torso.addCondition(name); break;
                case HEAD: Head.addCondition(name); break;
                case BLOOD: Blood.addCondition(name); break;
                default:
            }
        });

        conditions = ImmutableMap.copyOf(conditions);
        bloodConditions = ImmutableList.copyOf(bloodConditions);
        bodyConditions = ImmutableList.copyOf(bodyConditions);
        resistConditions = ImmutableList.copyOf(resistConditions);
    }

    private static void handlerInit() {
        DGH_API.conditionAPI.handlers().forEach((key, context) -> {
            int priority = context.getLeft();
            var handler = context.getMiddle();
            ComponentLevel level = context.getRight();

            switch (level) {
                case VISIBLE_BODY: AbstractVisibleBody.addHandler(priority, handler); break;
                case EXTREMITIES: AbstractExtremities.addHandler(priority, handler); break;
                case ARM: AbstractArm.addHandler(priority, handler); break;
                case LEG: AbstractLeg.addHandler(priority, handler); break;
                case TORSO: Torso.addHandler(priority, handler); break;
                case HEAD: Head.addHandler(priority, handler); break;
                case BLOOD: Blood.addHandler(priority, handler); break;
                default:
            }
        });
    }

    public static BodyCondition get(ResourceLocation location) {
        needFrozen();
        return conditions.get(location);
    }

    public static Map<ResourceLocation, BodyCondition> conditions() {
        needFrozen();
        return conditions;
    }

    public static List<ResourceLocation> bloodConditions() {
        needFrozen();
        return bloodConditions;
    }

    public static List<ResourceLocation> bodyConditions() {
        needFrozen();
        return bodyConditions;
    }

    public static List<ResourceLocation> resistConditions() {
        needFrozen();
        return resistConditions;
    }

    public static Phase phase() {
        return phase;
    }

    private static void needFrozen() {
        if (phase() != Phase.FROZEN) throw new RuntimeException("cannot operate before initialized");
    }

    public enum Phase {
        REGISTERING,
        BUILDING,
        FROZEN
    }
}
