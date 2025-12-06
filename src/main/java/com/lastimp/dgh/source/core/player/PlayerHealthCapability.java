
package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.api.bodyPart.AbstractArm;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.function.Function;

import static com.lastimp.dgh.api.enums.BodyComponents.*;
import static com.lastimp.dgh.api.enums.BodyCondition.INTENSE_PAIN;

public class PlayerHealthCapability implements INBTSerializable<CompoundTag> {
    private final WholeBody body = new WholeBody();
    private float playerVitality = 1.0f;
    private int slowDown = 0;
    private int armBreak = 0;
    private long livingTick = 0;
    private float almostDead = 1.0f;

    public static PlayerHealthCapability get(Player player) {
        return player.getData(ModCapabilities.PLAYER_HEALTH);
    }

    public static void set(Player player, PlayerHealthCapability capability) {
        player.setData(ModCapabilities.PLAYER_HEALTH, capability);
    }

    public static <T> T getAndSet(Player player, Function<PlayerHealthCapability, T> function) {
        PlayerHealthCapability health = PlayerHealthCapability.get(player);
        T result = function.apply(health);
        PlayerHealthCapability.set(player, health);
        return result;
    }

    public AbstractBody getComponent(BodyComponents component) {
        return this.body.getComponent(component);
    }

    public PlayerHealthCapability update(Player player) {
        this.body.update(this, player);
        this.updateLabels(player);
        return this;
    }

    private void updateLabels(Player player) {
        if (this.livingTick + 1 > 0) this.livingTick++;
        this.armBreak = (((AbstractArm)this.getComponent(LEFT_ARM)).available(this) ? 0 : 1) + (((AbstractArm)this.getComponent(RIGHT_ARM)).available(this) ? 0 : 1);
        this.slowDown = this.body.slowDownLevel(this);
        this.playerVitality = 1.0f - this.body.updateVitalityLost(this, player);

        float bloodVitalityLost = this.getComponent(BLOOD).updateVitalityLost(this, player);
        if (this.playerVitality + bloodVitalityLost < this.almostDead)
            this.almostDead = this.playerVitality + bloodVitalityLost;
    }

    public AbstractBody[] legs() {
        return new AbstractBody[] {
                this.body.getComponent(LEFT_LEG),
                this.body.getComponent(RIGHT_LEG)
        };
    }

    public AbstractBody[] arms() {
        return new AbstractBody[] {
                this.body.getComponent(LEFT_ARM),
                this.body.getComponent(RIGHT_ARM)
        };
    }

    public AbstractBody[] visibleParts() {
        return new AbstractBody[] {
                this.body.getComponent(HEAD),
                this.body.getComponent(TORSO),
                this.body.getComponent(LEFT_ARM),
                this.body.getComponent(RIGHT_ARM),
                this.body.getComponent(LEFT_LEG),
                this.body.getComponent(RIGHT_LEG),
        };
    }

    public boolean intensePain() {
        return  this.getComponent(LEFT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_ARM).abnormal(INTENSE_PAIN) ||
                this.getComponent(LEFT_LEG).abnormal(INTENSE_PAIN) ||
                this.getComponent(RIGHT_LEG).abnormal(INTENSE_PAIN);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = this.body.serializeNBT(provider);
        tag.putFloat("playerVitality", this.playerVitality);
        tag.putInt("slowDown", this.slowDown);
        tag.putInt("armBreak", this.armBreak);
        tag.putLong("livingTick", this.livingTick);
        tag.putFloat("almostDead", this.almostDead);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(provider, nbt);
        this.playerVitality = nbt.getFloat("playerVitality");
        this.slowDown = nbt.getInt("slowDown");
        this.armBreak = nbt.getInt("armBreak");
        this.livingTick = nbt.getLong("livingTick");
        this.almostDead = nbt.getFloat("almostDead");
    }

    public float playerVitality() {
        return playerVitality;
    }

    public int slowDown() {
        return slowDown;
    }

    public float almostDead() {
        return almostDead;
    }

    public void resetAlmostDead() {
        this.almostDead = 1.0f;
    }

    public long livingTick() {
        return livingTick;
    }

    public int armBreak() {
        return armBreak;
    }

    public static boolean isDying(Player player) {
        return player.getHealth() < 0.05 && !player.isDeadOrDying();
    }
}
