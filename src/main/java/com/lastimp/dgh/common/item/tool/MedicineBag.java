package com.lastimp.dgh.common.item.tool;

import com.lastimp.dgh.common.container.IBackpackInventory;
import com.lastimp.dgh.common.tags.ModTags;
import com.lastimp.dgh.common.item.bases.AbstractSmallBag;

public class MedicineBag extends AbstractSmallBag {
    public MedicineBag(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void initBag(IBackpackInventory inventory) {
        inventory.addAllowed(ModTags.MEDICINE_DIRECT);
    }
}
