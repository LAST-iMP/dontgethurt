
package com.lastimp.dgh.source.client.hotkey;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(value = Dist.CLIENT)
public class KeyBinding {
    public static final String KEY_CATEGORY_DGH = "key.category.dgh";

    public static final String KEY_HEALTH_MENU = "key.dgh.health_menu";
    public static final String KEY_GIVE_UP = "key.dgh.give_up";
    public static final String KEY_CALL_FOR_HELP = "key.dgh.call_for_help";

    public static final Set<KeyMapping> keys = new HashSet<>();

    public static final KeyMapping OPEN_MENU_KEY = addKey(KEY_HEALTH_MENU, GLFW.GLFW_KEY_O);
    public static final KeyMapping GIVE_UP =  addKey(KEY_GIVE_UP, GLFW.GLFW_KEY_ESCAPE);
//    public static final KeyMapping CALL_FOR_HELP =  addKey(KEY_CALL_FOR_HELP, GLFW.GLFW_KEY_H);

    private static KeyMapping addKey(String name, int key) {
        var newKey = new KeyMapping(
                name, KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM, key, KEY_CATEGORY_DGH
        );
        KeyBinding.keys.add(newKey);
        return newKey;
    }
}
