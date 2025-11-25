
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.network.ClientPayloadHandler;
import com.lastimp.dgh.network.ServerPayloadHandler;
import com.lastimp.dgh.source.client.gui.MenuProvider.HealthCareBagMenuProvider;
import com.lastimp.dgh.source.client.gui.MenuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MyKeyPressedData {
    private String key;
    private int index;

    public MyKeyPressedData(FriendlyByteBuf buffer) {
        this.key = buffer.readUtf();
        this.index = buffer.readInt();
    }

    public MyKeyPressedData(String key, int index) {
        this.key = key;
        this.index = index;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.key);
        buf.writeInt(this.index);
    }

    public static void handlerServer(final MyKeyPressedData data, Supplier<NetworkEvent.Context> ctx) {
        KeyPressedType key = KeyPressedType.valueOf(data.key());
        ServerPlayer player = ctx.get().getSender();
        if (key == KeyPressedType.KEY_HEALTH_MENU) {
            HealthMenuProvider.open(player, player.getUUID(), false);
        } else if (key == KeyPressedType.KEY_SLOT_USE) {
            var slot = player.getInventory().getItem(data.index());
            if (slot.is(ModItems.HEALTH_SCANNER.get()))
                HealthMenuProvider.open(player, player.getUUID(), true);
            if (slot.is(ModItems.HEALTH_CARE_BAG.get()))
                HealthCareBagMenuProvider.open(player, slot);
        }
    }

    public static MyKeyPressedData getInstance(KeyPressedType key, int index) {
        return new MyKeyPressedData(key.name(), index);
    }

    public String key() {
        return key;
    }

    public int index() {
        return index;
    }
}
