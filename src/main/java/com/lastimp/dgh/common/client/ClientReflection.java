package com.lastimp.dgh.common.client;

import com.lastimp.dgh.common.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ClientReflection {
    private ClientReflection() {}

    public static void setMissTime(int time) {
        Minecraft mc = Minecraft.getInstance();
        try {
            Field f = findField(mc.getClass(), "missTime");
            if (f != null) {
                f.setAccessible(true);
                f.setInt(mc, time);
                return;
            }
            Method m = findMethod(mc.getClass(), "setMissTime", int.class);
            if (m != null) {
                m.setAccessible(true);
                m.invoke(mc, time);
                return;
            }
        } catch (Throwable t) {
            Utils.LOGGER.error("Failed to set missTime via reflection", t);
        }
    }

    public static void setHandsBusy(LocalPlayer player, boolean value) {
        if (player == null) return;
        try {
            Field f = findField(player.getClass(), "handsBusy");
            if (f != null) {
                f.setAccessible(true);
                f.setBoolean(player, value);
                return;
            }
            Method m = findMethod(player.getClass(), "setHandsBusy", boolean.class);
            if (m != null) {
                m.setAccessible(true);
                m.invoke(player, value);
                return;
            }

            // try MethodHandle fallback for boolean field
            Field[] fields = player.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == boolean.class) {
                    field.setAccessible(true);
                    field.setBoolean(player, value);
                    return;
                }
            }
        } catch (Throwable t) {
            Utils.LOGGER.error("Failed to set handsBusy via reflection", t);
        }
    }

    private static Field findField(Class<?> cls, String... names) {
        for (String name : names) {
            try {
                return cls.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {}
        }
        // try common name
        try {
            for (Field f : cls.getDeclaredFields()) {
                if (f.getName().toLowerCase().contains("hand") || f.getName().toLowerCase().contains("busy") || f.getName().toLowerCase().contains("miss")) {
                    return f;
                }
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static Method findMethod(Class<?> cls, String name, Class<?>... params) {
        try {
            return cls.getDeclaredMethod(name, params);
        } catch (NoSuchMethodException ignored) {}
        return null;
    }
}
