package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.BagItemCapabilityProvider;
import com.lastimp.dgh.source.core.menu.menuProvider.SurgeryToolBagMenuProvider;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SurgeryToolBag extends Item {

    public SurgeryToolBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        BackpackInventory inventory = new BackpackInventory(9);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_SURGERY);

        return new BagItemCapabilityProvider(inventory, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide && usedHand == InteractionHand.MAIN_HAND)
            SurgeryToolBagMenuProvider.open(player, player.getMainHandItem());
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }
}
