package com.lastimp.dgh.common.enums;

import net.minecraft.network.chat.Component;

public enum OperationType {
    HEALTH_SCANN,
    BLOOD_SCANN,
    SYN
    ;
    @Override
    public String toString() {
        return Component.translatable(this.name()).getString();
    }
}
