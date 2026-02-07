package com.lastimp.dgh.common.network.message;

import com.lastimp.dgh.common.enums.BodyComponents;
import com.lastimp.dgh.common.network.IPayload;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class MyHealingItemUseData implements IPayload<MyHealingItemUseData> {
    public static final int HAND_PULSE = -255;
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

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeLong(this.id_most);
        buf.writeLong(this.id_least);
        buf.writeInt(this.slotNum);
        buf.writeUtf(this.component);
    }

    @Override
    public MyHealingItemUseData fromBytes(FriendlyByteBuf buf) {
        return new MyHealingItemUseData(buf);
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
