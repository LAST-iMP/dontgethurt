
package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.enums.KeyPressedType;
import net.minecraft.network.FriendlyByteBuf;

public class MyKeyPressedData {
    private KeyPressedType key;
    private int index;

    public MyKeyPressedData(FriendlyByteBuf buffer) {
        this.key = KeyPressedType.valueOf(buffer.readUtf());
        this.index = buffer.readInt();
    }

    public MyKeyPressedData(KeyPressedType key, int index) {
        this.key = key;
        this.index = index;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.key.name());
        buf.writeInt(this.index);
    }

    public static MyKeyPressedData getInstance(KeyPressedType key, int index) {
        return new MyKeyPressedData(key, index);
    }

    public KeyPressedType key() {
        return key;
    }

    public int index() {
        return index;
    }
}
