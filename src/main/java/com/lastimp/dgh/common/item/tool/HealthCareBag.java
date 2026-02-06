package com.lastimp.dgh.common.item.tool;

import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;

public class HealthCareBag extends AbstractSmallBag {

    public HealthCareBag(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void initBag(IBackpackInventory inventory) {
        inventory.addAllowed(ModTags.MEDICINE);
        inventory.addAllowed(ModTags.MEDICAL_TOOLS_BASIC);
    }
}
