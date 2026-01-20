package com.lastimp.dgh.source.item.bases;

import com.lastimp.dgh.source.core.menu.menuProvider.HealthSmallBagMenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AbstractSmallBag extends Item {
    public AbstractSmallBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    public abstract BackpackInventory getBackPackHandler(ItemStack bagStack);

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && usedHand == InteractionHand.MAIN_HAND)
            HealthSmallBagMenuProvider.open(player, player.getMainHandItem());
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
