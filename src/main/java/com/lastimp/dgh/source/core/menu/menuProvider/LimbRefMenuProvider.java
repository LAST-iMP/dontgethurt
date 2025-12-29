package com.lastimp.dgh.source.core.menu.menuProvider;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.menu.BagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class LimbRefMenuProvider implements MenuProvider {
    private final ItemStack bagStack;

    public LimbRefMenuProvider(ItemStack bagStack) {
        this.bagStack = bagStack;
    }

    public static void open(Player player, ItemStack itemStack) {
        player.openMenu(new LimbRefMenuProvider(itemStack), buf -> {
            buf.writeInt(player.getInventory().findSlotMatchingItem(itemStack));
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + DontGetHurt.MODID + ".limb_menu_tool_bag");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BagMenu.LimbRefBag(i, inventory, bagStack);
    }
}