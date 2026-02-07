package com.lastimp.dgh.fabric.capability;

import com.lastimp.dgh.fabric.capability.provider.BagItemInventoryProvider;

public interface BagHolder {
    BagItemInventoryProvider dgh$getBagProvider();
}
