package com.lastimp.dgh.common.item.tool;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AutoUseBag extends AbstractSmallBag {
    public AutoUseBag(Properties properties) {
        super(properties);
    }

    @Override
    public void initBag(IBackpackInventory inventory) {
        inventory.addAllowed(ModTags.MEDICINE);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_SURGERY);
        inventory.addDisAllowed(ModTags.MEDICAL_TOOLS_SMALL_BAGS);
    }

    public static NonNullList<ItemStack> getContext(ItemStack itemStack) {
        return PlatformService.BACKPACK_FACTORY.getContext(itemStack);
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
