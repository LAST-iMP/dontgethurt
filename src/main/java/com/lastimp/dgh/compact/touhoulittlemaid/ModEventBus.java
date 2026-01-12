package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTombstoneEvent;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModEventBus {
    public static void onMaidTombstone(MaidTombstoneEvent event) {
        var maid = event.getMaid();
        HealthCapability.getAndApply(maid, h -> {
            h.clearLastDeathDirectInjury();
            h.addToLastDeathDirectInjury(h.directInjury());
            h.clearDirectInjury();
        });

        var tombStone = event.getTombstone();
        var stack = new ItemStack(Items.WRITTEN_BOOK, 1);
        var name = Component.translatable(maid.getName().getString());
        if (HealthCapability.getAndApply(maid, health -> health.write(stack, name, name), false)) {
            tombStone.insertItem(stack);
        }
    }
}
