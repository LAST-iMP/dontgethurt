/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.Register.ModCapabilities;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.source.core.bodyPart.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.function.Function;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class PlayerHealthCapability implements INBTSerializable<CompoundTag> {
    private final WholeBody body = new WholeBody();
    protected ServerPlayer player;
    private float playerVitality = 1.0f;

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
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return this.body.serializeNBT(provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        this.body.deserializeNBT(provider, nbt);
    }

    public float playerVitality() {
        return playerVitality;
    }

    public void setPlayerVitality(float playerVitality) {
        this.playerVitality = Mth.clamp(playerVitality, 0.0f, 1.0f);
    }
}
