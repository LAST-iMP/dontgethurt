package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.capability.BagItemCapabilityProvider;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BagHealerHandler {
    public static void handleBagHealing(ItemStack stack, ServerPlayer player, LivingEntity target, BodyComponents component) {
        BackpackInventory inv = (BackpackInventory) BagItemCapabilityProvider.getBackPackHandler(stack);
        for (int i = 0; i < inv.getSlots(); i++) {
            var slotItem = inv.getStackInSlot(i);
            if (HealingHandler.useItemOn(slotItem, player, target, component)) break;
        }
        var bagTag = stack.getOrCreateTag();
        bagTag.put("inv", inv.serializeNBT());
    }
}
