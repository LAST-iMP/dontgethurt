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

package com.lastimp.dgh.source.item;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractDirectHealItems;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class BloodPacksEmpty extends AbstractDirectHealItems {
    public BloodPacksEmpty(Properties properties) {
        super(properties);
    }

    @Override
    public boolean heal(@NotNull ServerPlayer source, @NotNull ServerPlayer target) {
        return PlayerHealthCapability.getAndSet(target, health -> {
            PlayerBlood blood = (PlayerBlood) health.getComponent(BodyComponents.BLOOD);
            float currCondition = blood.getConditionValue(BLOOD_LOSS);
            if (currCondition > BLOOD_LOSS.maxValue - 0.3f) return false;

            blood.injury(BLOOD_LOSS, 0.25f);
            if (!source.getStringUUID().equals(target.getStringUUID()))
                source.attack(target);

            ItemStack stack = new ItemStack(ModItems.BLOOD_PACK.get());
            if (!source.addItem(stack)) {
                source.drop(stack, true, true);
            }
            return true;
        });
    }

    @Override
    protected BodyComponents getAvaComponent() {
        return BodyComponents.BLOOD;
    }
}
