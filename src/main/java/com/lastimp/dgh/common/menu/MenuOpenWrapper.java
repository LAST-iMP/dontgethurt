package com.lastimp.dgh.common.menu;

import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.common.menu.menuProvider.HealthSmallBagMenuProvider;
import com.lastimp.dgh.common.entry.register.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class MenuOpenWrapper {
    public static void openMenu(ItemStack stack, ServerPlayer player) {
        if (stack.is(ModItems.HEALTH_SCANNER.get()))
            openHealthMenu(player, player.getUUID(), true);
        else if (stack.is(ModItems.MEDICINE_BAG.get())) {
            openBag(player, stack, new HealthSmallBagMenuProvider.MedicineSmallBagMenuProvider(stack));
        } else if (stack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS)) {
            openBag(player, stack, new HealthSmallBagMenuProvider(stack));
        }
    }

    private static void openBag(Player player, ItemStack itemStack, MenuProvider menuProvider) {
        player.openMenu(menuProvider, buf -> {
            buf.writeInt(player.getInventory().findSlotMatchingItem(itemStack));
        });
    }

    public static void openHealthMenu(Player player, UUID targetPlayer, boolean isDevice) {
        player.openMenu(new HealthMenuProvider(targetPlayer, isDevice), buf -> {
            buf.writeUUID(targetPlayer);
            buf.writeBoolean(isDevice);
        });
    }
}
