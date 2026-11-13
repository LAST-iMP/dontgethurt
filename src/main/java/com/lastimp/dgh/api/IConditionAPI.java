package com.lastimp.dgh.api;

import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.function.Consumer;

public interface IConditionAPI {
    BodyCondition.ConditionBuilder registerCondition(ResourceLocation name, ResourceLocation texture, ComponentLevel components);
    Map<ResourceLocation, BodyCondition.ConditionBuilder> conditions();
    void registerHandler(ResourceLocation key, int priority, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>> handler, ComponentLevel components);
    Map<ResourceLocation, Triple<Integer, Consumer<Triple<HealthCapability, LivingEntity, ? extends AbstractBody>>, ComponentLevel>> handlers();
}
