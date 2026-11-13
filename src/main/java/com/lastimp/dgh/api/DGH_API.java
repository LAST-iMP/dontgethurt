package com.lastimp.dgh.api;

import com.lastimp.dgh.api.impl.ConditionAPI;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.function.Consumer;

public abstract class DGH_API {
    public static final IConditionAPI conditionAPI = new ConditionAPI();

    public static BodyCondition.ConditionBuilder registerCondition(ResourceLocation name, ResourceLocation texture, ComponentLevel components) {
        return conditionAPI.registerCondition(name, texture, components);
    }

    public static Map<ResourceLocation, BodyCondition.ConditionBuilder> conditions() {
        return conditionAPI.conditions();
    }

    public static void registerHandler(ResourceLocation key, int priority, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler, ComponentLevel components) {
        conditionAPI.registerHandler(key, priority, handler, components);
    }

    public static Map<ResourceLocation, Triple<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>, ComponentLevel>> handlers() {
        return conditionAPI.handlers();
    }

}
