package com.lastimp.dgh.common.item.bases;

import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.menu.MenuOpenWrapper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractSmallBag extends Item {

    public AbstractSmallBag(Properties p_41383_) {
        super(p_41383_);
    }

    public abstract void initBag(IBackpackInventory inventory);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && usedHand == InteractionHand.MAIN_HAND)
            MenuOpenWrapper.openMenu(player.getMainHandItem(), (ServerPlayer) player);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
