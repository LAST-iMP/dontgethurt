
package com.lastimp.dgh.network.message;

import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.source.client.gui.menuProvider.HealthCareBagMenuProvider;
import com.lastimp.dgh.source.client.gui.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.client.gui.menuProvider.SurgeryToolBagMenuProvider;
import com.lastimp.dgh.source.core.player.DyingHandler;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

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
        switch (key) {
            case KEY_HEALTH_MENU:
                HealthMenuProvider.open(player, player.getUUID(), false);
                break;
            case KEY_SLOT_USE:
                var slot = player.getInventory().getItem(data.index());
                if (slot.is(ModItems.HEALTH_SCANNER.get()))
                    HealthMenuProvider.open(player, player.getUUID(), true);
                if (slot.is(ModItems.HEALTH_CARE_BAG.get()))
                    HealthCareBagMenuProvider.open(player, slot);
                if (slot.is(ModItems.SURGERY_TOOL_BAG.get()))
                    SurgeryToolBagMenuProvider.open(player, slot);
                break;
            case GIVE_UP:
                DyingHandler.setPlayerDead(player);
                break;
            case CALL_FOR_HELP:
                player.getServer().getPlayerList().getPlayers().forEach(p -> {
                    p.sendSystemMessage(Component.literal(
                            player.getScoreboardName() + "在("
                                    + String.format("%.1f", player.position().x) + ", "
                                    + String.format("%.1f", player.position().y) + ", "
                                    + String.format("%.1f", player.position().z) + ")需要救助"
                    ));
                });
                break;
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
