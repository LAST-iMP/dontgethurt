
package com.lastimp.dgh.common.enums;


import net.minecraft.network.chat.Component;

import java.util.List;

public enum BodyComponents {
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG,
    HEAD,
    TORSO,

    BLOOD;

    public static final List<BodyComponents> VISIBLE_BODIES = List.of(
            HEAD, TORSO, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
    );

    public static final List<BodyComponents> EXTREMITIES = List.of(
            LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG
    );

    public static final List<BodyComponents> LEGS = List.of(
            LEFT_LEG, RIGHT_LEG
    );

    @Override
    public String toString() {
        return getComponent().getString();
    }

    public Component getComponent() {
        return Component.translatable(this.name());
    }

    public static BodyComponents random() {
        return values()[(int) (Math.random() * 6)];
    }
}