package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.capability.BagItemCapabilityProvider;
import com.lastimp.dgh.source.item.bases.AbstractSmallBag;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class MedicineBag extends AbstractSmallBag {
    public MedicineBag(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        BackpackInventory inventory = new BackpackInventory(9);
        inventory.addAllowed(ModTags.MEDICINE_DIRECT);

        return new BagItemCapabilityProvider(inventory, stack);
    }
}
