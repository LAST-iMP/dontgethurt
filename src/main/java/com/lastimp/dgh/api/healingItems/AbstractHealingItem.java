package com.lastimp.dgh.api.healingItems;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractHealingItem extends Item {

    public AbstractHealingItem(Properties properties) {
        super(properties);
    }

    public boolean available(LivingEntity target, ItemStack stack) {
        if (this instanceof AbstractHealingEquipment) return true;
        var health = HealthCapability.get(target);
        return !health.autoPulse().getStackInSlot(0).is(ModItems.STASIS_BAG.get());
    }
}
