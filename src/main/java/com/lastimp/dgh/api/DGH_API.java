package com.lastimp.dgh.api;

import com.lastimp.dgh.api.impl.ConditionAPI;
import com.lastimp.dgh.api.impl.HealingAPI;
import com.lastimp.dgh.common.capability.HealthCapability;
import com.lastimp.dgh.common.capability.bodyPart.base.AbstractBody;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.capability.healthCore.healingSystem.HealingResult;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.menu.BagMenu;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public abstract class DGH_API {
    public static final IConditionAPI conditionAPI = new ConditionAPI();
    public static final IHealingAPI healingAPI = new HealingAPI();

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

    public static void handleValindaHealing(LivingEntity entity, float amount) {
        healingAPI.handleValindaHealing(entity, amount);
    }

    public static HealingResult handleHealthMenuItemUse(HealthMenu healthMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        return healingAPI.handleHealthMenuItemUse(healthMenu, data, sourcePlayer, target);
    }

    public static HealingResult handleMedicineBagMenuItemUse(BagMenu.MedicineSmallBag bagMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        return healingAPI.handleMedicineBagMenuItemUse(bagMenu, data, sourcePlayer, target);
    }

    public static HealingResult useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        return healingAPI.useItemOn(itemStack, source, target, component);
    }

    public static HealingResult handleBagHealing(ItemStack stack, ServerPlayer player, LivingEntity target, BodyComponents component) {
        return healingAPI.handleBagHealing(stack, player, target, component);
    }

}
