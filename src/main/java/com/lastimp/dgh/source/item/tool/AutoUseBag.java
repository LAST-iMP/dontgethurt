package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.BagItemCapabilityProvider;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class AutoUseBag extends AbstractSmallBag {
    public AutoUseBag(Properties properties) {
        super(properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        BackpackInventory inventory = new BackpackInventory(9);
        inventory.addAllowed(ModTags.MEDICINE);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_SURGERY);
        inventory.addDisAllowed(ModTags.MEDICAL_TOOLS_SMALL_BAGS);

        return new BagItemCapabilityProvider(inventory, stack);
    }

    public static NonNullList<ItemStack> getContext(ItemStack itemStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        BackpackInventory inv = (BackpackInventory) BagItemCapabilityProvider.getBackPackHandler(itemStack);
        for (int i = 0; i < inv.getSlots(); i++) {
            var slotItem = inv.getStackInSlot(i);
            nonnulllist.add(i, slotItem);
        }
        return nonnulllist;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack itemStack) {
        return Optional.of(new Tooltip(getContext(itemStack)));
    }

    public static class Tooltip implements TooltipComponent {
        private final NonNullList<ItemStack> items;

        public Tooltip(NonNullList<ItemStack> items) {
            this.items = items;
        }

        public NonNullList<ItemStack> getItems() {
            return this.items;
        }
    }
}
