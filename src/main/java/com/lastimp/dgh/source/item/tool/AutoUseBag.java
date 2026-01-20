package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoUseBag extends AbstractSmallBag {
    public AutoUseBag(Properties properties) {
        super(properties);
    }

    @Override
    public BackpackInventory getBackPackHandler(ItemStack bagStack) {
        var backpack = new BackpackInventory(bagStack, DataComponents.CONTAINER, 9);
        backpack.addAllowed(ModTags.MEDICINE);
        backpack.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        backpack.addAllowed(ModTags.MEDICAL_TOOLS_SURGERY);
        backpack.addDisAllowed(ModTags.MEDICAL_TOOLS_SMALL_BAGS);
        return backpack;
    }

    public static NonNullList<ItemStack> getContext(ItemStack itemStack) {
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        BackpackInventory inv = new BackpackInventory(itemStack, DataComponents.CONTAINER, 9);
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
