package com.lastimp.dgh.common.client.hotkey;

import net.minecraft.client.KeyMapping;

public interface IKeyHelper {
    KeyMapping getKey(String name, int key);
}
