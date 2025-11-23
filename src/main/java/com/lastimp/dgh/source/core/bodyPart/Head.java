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
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.world.entity.player.Player;

import java.util.List;

import static com.lastimp.dgh.api.enums.BodyCondition.WITHDRAW;

public class Head extends AbstractVisibleBody {
    private static List<BodyCondition> HEAD_CONDITIONS;

    public Head() {
        super();
    }

    @Override
    public AbstractBody update(PlayerHealthCapability health, Player player) {
        return super.update(health, player);
    }

    public Head(Void v) {
        this();
    }

    @Override
    public List<BodyCondition> getBodyConditions() {
        if (HEAD_CONDITIONS == null) {
            HEAD_CONDITIONS = super.getBodyConditions();
            HEAD_CONDITIONS.addAll(List.of(
                    WITHDRAW
            ));
        }
        return HEAD_CONDITIONS;
    }
}
