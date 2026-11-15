package com.lastimp.dgh.api;

import com.lastimp.dgh.common.capability.healthCore.healingSystem.HealingResult;
import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.menu.BagMenu;
import com.lastimp.dgh.common.menu.HealthMenu;
import com.lastimp.dgh.common.network.message.MyHealingItemUseData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IHealingAPI {
    void handleValindaHealing(LivingEntity entity, float amount);
    HealingResult handleHealthMenuItemUse(HealthMenu healthMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target);
    HealingResult handleMedicineBagMenuItemUse(BagMenu.MedicineSmallBag bagMenu, final MyHealingItemUseData data, ServerPlayer sourcePlayer, LivingEntity target);
    HealingResult useItemOn(ItemStack itemStack, @NotNull ServerPlayer source, LivingEntity target, BodyComponents component);
    HealingResult handleBagHealing(ItemStack stack, ServerPlayer player, LivingEntity target, BodyComponents component);
}
