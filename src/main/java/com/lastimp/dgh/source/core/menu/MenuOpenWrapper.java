package com.lastimp.dgh.source.core.menu;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthSmallBagMenuProvider;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

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
        NetworkHooks.openScreen((ServerPlayer) player, menuProvider, buf -> buf.writeItem(itemStack));
    }

    public static void openHealthMenu(Player player, UUID targetPlayer, boolean isDevice) {
        NetworkHooks.openScreen(
                (ServerPlayer) player,
                new HealthMenuProvider(targetPlayer, isDevice),
                buf -> {
                    buf.writeUUID(targetPlayer);
                    buf.writeBoolean(isDevice);
                });
    }
}
