package com.lastimp.dgh.source.core.menu.menuProvider;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.menu.BagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class HealthSmallBagMenuProvider implements MenuProvider {
    private final ItemStack bagStack;

    public HealthSmallBagMenuProvider(ItemStack bagStack) {
        this.bagStack = bagStack;
    }

    public static void open(Player player, ItemStack itemStack) {
        NetworkHooks.openScreen((ServerPlayer) player,
                new HealthSmallBagMenuProvider(itemStack),
                buf -> buf.writeItem(itemStack)
        );
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui." + DontGetHurt.MODID + ".health_small_bag");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BagMenu.HealthSmallBag(i, inventory, bagStack);
    }
}
