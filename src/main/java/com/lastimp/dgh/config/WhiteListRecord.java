package com.lastimp.dgh.config;

public record WhiteListRecord(
        float DOWN_DAMAGE_RESISTANCE_ENV,
        float DOWN_DAMAGE_RESISTANCE_ENTITY,
        float DOWN_DAMAGE_RESISTANCE_PLAYER,
        boolean CAN_LIE_DOWN
) {
    public static final WhiteListRecord DEFAULT = new WhiteListRecord(0.1f, 0.1f, 0.1f, true);
}
