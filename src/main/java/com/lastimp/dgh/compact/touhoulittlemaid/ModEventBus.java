package com.lastimp.dgh.compact.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidAndItemTransformEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTombstoneEvent;
import com.github.tartaricacid.touhoulittlemaid.item.ItemFilm;
import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModEventBus {
    public static void onMaidTombstone(MaidTombstoneEvent event) {
        var maid = event.getMaid();
        if (!HealthCapability.has(maid)) return;
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

    public static void onMaidItemTransfer(MaidAndItemTransformEvent.ToItem event) {
        if (!canRecordToFilm(event)) return;
        HealthCapability.getAndApply(event.getMaid(), h ->
                event.getData().put(HealthCapability.HEALTH_RECORD, h.deathSerializeNBT(event.getMaid().registryAccess()))
        );
    }

    public static void onItemMaidTransfer(MaidAndItemTransformEvent.ToMaid event) {
        if (!canRecordToFilm(event)) return;
        HealthCapability.getAndApply(event.getMaid(), h ->
                h.respawnDeserializeNBT(event.getMaid().registryAccess(), event.getData().getCompound(HealthCapability.HEALTH_RECORD))
        );
    }

    private static boolean canRecordToFilm(MaidAndItemTransformEvent event) {
        if (!(event.getItem().getItem() instanceof ItemFilm)) return false;
        return HealthCapability.has(event.getMaid());
    }
}
