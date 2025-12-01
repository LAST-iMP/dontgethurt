package com.lastimp.dgh.source.client.gui.MenuProvider;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.client.gui.BagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HealthCareBagMenuProvider implements MenuProvider {
    private final ItemStack bagStack;

    public HealthCareBagMenuProvider(ItemStack bagStack) {
        this.bagStack = bagStack;
    }

    public static void open(Player player, ItemStack itemStack) {
        player.openMenu(new HealthCareBagMenuProvider(itemStack), buf -> {
            buf.writeInt(player.getInventory().findSlotMatchingItem(itemStack));
        });
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + DontGetHurt.MODID + ".health_care_bag");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BagMenu.HealthCareBag(i, inventory, bagStack);
    }
}
