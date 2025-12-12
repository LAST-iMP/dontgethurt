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

package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.source.client.gui.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class HealthScanner extends AbstractHealingItem {
    private static List<ResourceLocation> HEALTH_SCANNER_CONDITIONS;
    private static List<ResourceLocation> EYESIGHT_CONDITIONS;

    public HealthScanner(Properties properties) {
        super(properties);
    }

    public static List<ResourceLocation> healthScannerConditions() {
        if (HEALTH_SCANNER_CONDITIONS == null) {
            HEALTH_SCANNER_CONDITIONS = new LinkedList<>();
            HEALTH_SCANNER_CONDITIONS.addAll(injuryConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(surgeryConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(painConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(comfortConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(resistConditions);
        }
        return HEALTH_SCANNER_CONDITIONS;
    }

    public static List<ResourceLocation> eyesightConditions() {
        healthScannerConditions();
        if (EYESIGHT_CONDITIONS == null) {
            EYESIGHT_CONDITIONS = new LinkedList<>();
            for (var condition : HEALTH_SCANNER_CONDITIONS) {
                if (eyeVisible.contains(condition))
                    EYESIGHT_CONDITIONS.add(condition);
            }
        }
        return EYESIGHT_CONDITIONS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (usedHand == InteractionHand.OFF_HAND)
            return InteractionResultHolder.pass(player.getItemInHand(usedHand));
        if (!level.isClientSide) {
            HealthMenuProvider.open(player, player.getUUID(), true);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof Player)) {
            return InteractionResult.PASS;
        }
        if (!player.level().isClientSide) {
            HealthMenuProvider.open(player, target.getUUID(), true);
        }
        return InteractionResult.SUCCESS;
    }


}
