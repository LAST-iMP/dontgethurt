package com.lastimp.dgh.common.item.tool;

import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;

public class LimbRefBeg extends AbstractSmallBag {

    public LimbRefBeg(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void initBag(IBackpackInventory inventory) {
        inventory.addAllowed(ModTags.MEDICAL_LIMBS);
    }
}
