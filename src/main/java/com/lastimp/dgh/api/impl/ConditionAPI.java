package com.lastimp.dgh.api.impl;

import com.google.common.collect.ImmutableMap;
import com.lastimp.dgh.api.ComponentLevel;
import com.lastimp.dgh.api.IConditionAPI;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.bodyPart.ConditionAccessor;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

public class ConditionAPI implements IConditionAPI {
    private final Map<ResourceLocation, BodyCondition.ConditionBuilder> conditions = new HashMap<>();
    private final Map<ResourceLocation, Triple<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>, ComponentLevel>> handlers = new HashMap<>();

    @Override
    public BodyCondition.ConditionBuilder registerCondition(ResourceLocation key, ResourceLocation texture, ComponentLevel component) {
        if (ConditionAccessor.phase() != ConditionAccessor.Phase.REGISTERING)
            throw new RejectedExecutionException("cannot register after initialized: " + key);

        if (conditions.containsKey(key))
            throw new IllegalArgumentException("Body condition already registered: " + key);

        var builder = BodyCondition.create(key, texture, component);
        conditions.put(key, builder);
        return builder;
    }

    @Override
    public void registerHandler(ResourceLocation key, int priority, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler, ComponentLevel component) {
        if (ConditionAccessor.phase() != ConditionAccessor.Phase.REGISTERING)
            throw new RejectedExecutionException("cannot register after initialized: " + key);

        if (conditions.containsKey(key))
            throw new IllegalArgumentException("Body condition handler already registered: " + key);

        handlers.put(key, Triple.of(priority, handler, component));
    }

    public Map<ResourceLocation, BodyCondition.ConditionBuilder> conditions() {
        return ImmutableMap.copyOf(conditions);
    }

    @Override
    public Map<ResourceLocation, Triple<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>, ComponentLevel>> handlers() {
        return ImmutableMap.copyOf(handlers);
    }
}
