package com.lastimp.dgh.fabric.client;

import com.lastimp.dgh.common.client.hotkey.IKeyHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import static com.lastimp.dgh.common.client.hotkey.KeyBinding.KEY_CATEGORY_DGH;

public class KeyHelperNF implements IKeyHelper {
    @Override
    public KeyMapping getKey(String name, int key) {
        return new KeyMapping(
                name, InputConstants.Type.KEYSYM, key, KEY_CATEGORY_DGH
        );
    }
}
