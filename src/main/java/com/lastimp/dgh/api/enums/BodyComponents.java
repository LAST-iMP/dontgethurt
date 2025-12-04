
package com.lastimp.dgh.api.enums;


import net.minecraft.network.chat.Component;

public enum BodyComponents {
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG,
    HEAD,
    TORSO,

    BLOOD,

    WHOLE_BODY;

    private static final BodyComponents[] VISIBLE_BODIES = new BodyComponents[]{
            LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG, HEAD, TORSO
    };

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

    public static BodyComponents[] getVisibleBodies() {
        return VISIBLE_BODIES;
    }
}