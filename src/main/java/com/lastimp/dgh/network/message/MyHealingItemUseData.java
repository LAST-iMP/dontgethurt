package com.lastimp.dgh.network.message;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.ServerPayloadHandler;
import com.lastimp.dgh.source.client.gui.menu.HealthMenu;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MyHealingItemUseData {
    private long id_most;
    private long id_least;
    private int slotNum;
    private String component;

    public MyHealingItemUseData(FriendlyByteBuf buffer) {
        this.id_most = buffer.readLong();
        this.id_least = buffer.readLong();
        this.slotNum = buffer.readInt();
        this.component = buffer.readUtf();
    }

    public MyHealingItemUseData(UUID uuid, int slotNum, String components) {
        this.id_most = uuid.getMostSignificantBits();
        this.id_least = uuid.getLeastSignificantBits();
        this.slotNum = slotNum;
        this.component = components;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(this.id_most);
        buf.writeLong(this.id_least);
        buf.writeInt(this.slotNum);
        buf.writeUtf(this.component);
    }

    public static void handlerServer(final MyHealingItemUseData data, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer sourcePlayer = ctx.get().getSender();
        if (!(sourcePlayer.containerMenu instanceof HealthMenu healthMenu)) return;
        ItemStack stack = healthMenu.getStackBySlotNum(data.slotNum());
        if (stack.is(ModTags.MEDICAL_TOOLS_BAGS)) {
            healthMenu.openBag(stack);
        } else {
            UUID targetID = new UUID(data.id_most(), data.id_least());
            var target = Utils.getLivingWithHealth(ctx.get().getSender().serverLevel(), targetID);
            if (target == null) return;
            BodyComponents component = data.component().equals("NONE") ? null : BodyComponents.valueOf(data.component());
            HealingHandler.useItemOn(stack, sourcePlayer, target, component);
        }
    }

    public static MyHealingItemUseData getInstance(UUID targetId, int slotNum, BodyComponents components) {
        if (components == null)
            return new MyHealingItemUseData(targetId, slotNum, "NONE");
        else
            return new MyHealingItemUseData(targetId, slotNum, components.name());
    }

    public String component() {
        return component;
    }

    public long id_least() {
        return id_least;
    }

    public long id_most() {
        return id_most;
    }

    public int slotNum() {
        return slotNum;
    }
}
