package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthCareBagMenuProvider;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import com.lastimp.dgh.source.item.bases.AbstractMedicalBags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealthCareBag extends AbstractMedicalBags {

    public HealthCareBag(Properties properties) {
        super(properties);
    }

    @Override
    public BackpackInventory getBackPackHandler(ItemStack bagStack) {
        var backpack = new BackpackInventory(bagStack, DataComponents.CONTAINER, 9);
        backpack.addAllowed(ModTags.MEDICINE);
        backpack.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        return backpack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && usedHand == InteractionHand.MAIN_HAND)
            HealthCareBagMenuProvider.open(player, player.getMainHandItem());
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
