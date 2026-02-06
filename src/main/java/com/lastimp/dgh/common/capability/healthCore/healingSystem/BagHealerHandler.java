package com.lastimp.dgh.common.capability.healthCore.healingSystem;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import com.lastimp.dgh.common.container.IBackpackInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BagHealerHandler {
    public static void handleBagHealing(ItemStack stack, ServerPlayer player, LivingEntity target, BodyComponents component) {
        IBackpackInventory inv = ((AbstractSmallBag)stack.getItem()).getBackPackHandler(stack);
        for (int i = 0; i < inv.getSlots(); i++) {
            var slotItem = inv.getStackInSlot(i);
            if (HealingHandler.useItemOn(slotItem, player, target, component)) {
                inv.setStackInSlot(i, slotItem);
                break;
            }
        }
    }
}
