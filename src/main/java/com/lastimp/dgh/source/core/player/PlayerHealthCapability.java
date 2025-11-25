
package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Function;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class PlayerHealthCapability implements INBTSerializable<CompoundTag> {
    private final WholeBody body = new WholeBody();
    private float playerVitality = 1.0f;
    private int slowDown = 0;

    public static PlayerHealthCapability get(Player player) {
        return player.getCapability(ModCapabilities.PLAYER_HEALTH).orElse(new PlayerHealthCapability());
    }

    public static void set(Player player, PlayerHealthCapability capability) {
        return;
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
        this.playerVitality = 1.0f - this.body.updateVitalityLost(this, player);
        this.slowDown = this.body.slowDownLevel();
        return this;
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

    @Override
    public CompoundTag serializeNBT() {
        return this.body.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(nbt);
    }

    public float playerVitality() {
        return playerVitality;
    }

    public int slowDown() {
        return slowDown;
    }

    public void setPlayerVitality(float playerVitality) {
        this.playerVitality = Mth.clamp(playerVitality, 0.0f, 1.0f);
    }
}
