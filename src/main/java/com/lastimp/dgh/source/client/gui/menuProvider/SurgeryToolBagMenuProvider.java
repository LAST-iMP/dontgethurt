package com.lastimp.dgh.source.client.gui.menuProvider;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.menu.BagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SurgeryToolBagMenuProvider implements MenuProvider {
    private final ItemStack bagStack;

    public SurgeryToolBagMenuProvider(ItemStack bagStack) {
        this.bagStack = bagStack;
    }

    public static void open(Player player, ItemStack itemStack) {
        player.openMenu(new SurgeryToolBagMenuProvider(itemStack), buf -> {
            buf.writeInt(player.getInventory().findSlotMatchingItem(itemStack));
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + DontGetHurt.MODID + ".surgery_tool_bag");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BagMenu.SurgeryToolBag(i, inventory, bagStack);
    }
}
