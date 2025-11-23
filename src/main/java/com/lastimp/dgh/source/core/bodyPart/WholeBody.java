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

package com.lastimp.dgh.source.core.bodyPart;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.BodyCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;

import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class WholeBody extends AbstractBody {
    private final HashMap<BodyComponents, AbstractBody> components = new HashMap<>();
    private static List<BodyCondition> WHOLE_BODY_CONDITIONS;

    public WholeBody() {
        components.put(LEFT_ARM, new LeftArm());
        components.put(RIGHT_ARM, new RightArm());
        components.put(LEFT_LEG, new LeftLeg());
        components.put(RIGHT_LEG, new RightLeg());
        components.put(HEAD, new Head());
        components.put(TORSO, new Torso());
        components.put(BLOOD, new PlayerBlood());
    }

    public AbstractBody getComponent(BodyComponents component) {
        return component == WHOLE_BODY ? this : components.get(component);
    }

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (WHOLE_BODY_CONDITIONS == null) {
            WHOLE_BODY_CONDITIONS = List.of();
        }
        return WHOLE_BODY_CONDITIONS;
    }

    @Override
    public float getVitalityWeight() {
        return 1;
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        this.updateComponent(LEFT_ARM, health, player);
        this.updateComponent(RIGHT_ARM, health, player);
        this.updateComponent(LEFT_LEG, health, player);
        this.updateComponent(RIGHT_LEG, health, player);
        this.updateComponent(HEAD, health, player);
        this.updateComponent(TORSO, health, player);
        this.updateComponent(BLOOD, health, player);
        return this;
    }

    private void updateComponent(BodyComponents component, PlayerHealthCapability health, Player player) {
        components.get(component).updatePre(health, player).update(health, player).updatePost(health, player);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag wholeBody = super.serializeNBT(provider);
        CompoundTag tag = new CompoundTag();
        for (BodyComponents comp : components.keySet()) {
            tag.put(comp.name(), components.get(comp).serializeNBT(provider));
        }
        tag.put(WHOLE_BODY.name(), wholeBody);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        components.put(LEFT_ARM, AbstractBody.buildFromNBT(provider, nbt.getCompound(LEFT_ARM.name()), LeftArm::new));
        components.put(RIGHT_ARM, AbstractBody.buildFromNBT(provider, nbt.getCompound(RIGHT_ARM.name()), RightArm::new));
        components.put(LEFT_LEG, AbstractBody.buildFromNBT(provider, nbt.getCompound(LEFT_LEG.name()), LeftLeg::new));
        components.put(RIGHT_LEG, AbstractBody.buildFromNBT(provider, nbt.getCompound(RIGHT_LEG.name()), RightLeg::new));
        components.put(HEAD, AbstractBody.buildFromNBT(provider, nbt.getCompound(HEAD.name()), Head::new));
        components.put(TORSO, AbstractBody.buildFromNBT(provider, nbt.getCompound(TORSO.name()), Torso::new));
        components.put(BLOOD, AbstractBody.buildFromNBT(provider, nbt.getCompound(BLOOD.name()), PlayerBlood::new));
        super.deserializeNBT(provider, nbt.getCompound(WHOLE_BODY.name()));
    }
}
