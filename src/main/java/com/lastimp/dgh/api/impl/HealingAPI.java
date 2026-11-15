package com.lastimp.dgh.api.impl;

import com.lastimp.dgh.api.IHealingAPI;
import com.lastimp.dgh.common.capability.healthCore.healingSystem.BagHealerHandler;
import com.lastimp.dgh.common.capability.healthCore.healingSystem.HealingHandler;
import com.lastimp.dgh.common.capability.healthCore.healingSystem.HealingResult;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.menu.BagMenu;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class HealingAPI implements IHealingAPI {
    public void handleValindaHealing(LivingEntity entity, float amount) {
        HealingHandler.handleValindaHealing(entity, amount);
    }

    public HealingResult handleHealthMenuItemUse(HealthMenu healthMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        return HealingHandler.handleHealthMenuItemUse(healthMenu, data, sourcePlayer, target);
    }

    public HealingResult handleMedicineBagMenuItemUse(BagMenu.MedicineSmallBag bagMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target) {
        return HealingHandler.handleMedicineBagMenuItemUse(bagMenu, data, sourcePlayer, target);
    }

    public HealingResult useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component) {
        return HealingHandler.useItemOn(itemStack, source, target, component);
    }

    public HealingResult handleBagHealing(ItemStack stack, ServerPlayer player, LivingEntity target, BodyComponents component) {
        return BagHealerHandler.handleBagHealing(stack, player, target, component);
    }
}
