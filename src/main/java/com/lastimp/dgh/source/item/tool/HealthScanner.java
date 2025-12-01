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
import com.lastimp.dgh.source.client.gui.MenuProvider.HealthMenuProvider;
import com.lastimp.dgh.api.enums.BodyCondition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class HealthScanner extends AbstractHealingItem {
    private static List<BodyCondition> HEALTH_SCANNER_CONDITIONS;
    private static List<BodyCondition> EYESIGHT_CONDITIONS;

    public HealthScanner(Properties properties) {
        super(properties);
    }

    public static List<BodyCondition> healthScannerConditions() {
        if (HEALTH_SCANNER_CONDITIONS == null) {
            HEALTH_SCANNER_CONDITIONS = List.of(
                    SURGERY_INCISION,
                    CLAMPED_BLEEDING,
                    RETRACTED_SKIN,
                    DRILLED_BONES,

                    BURN,
                    INTERNAL_INJURY,
                    OPEN_WOUND,
                    BLEED,
                    INFECTION,
                    FOREIGN_OBJECT,

                    BANDAGED,
                    BANDAGED_DIRTY,
                    OINMENTED,

                    DISLOCATION,
                    FRACTURE,
                    INTENSE_PAIN,
                    PLASTER_CAST,

                    ANALGESIA,
                    RESPIRATORY_ARREST,

                    WITHDRAW,
                    TRAUMATIC_SHOCK,
                    BRAIN_DAMAGE,

                    BURN_RES,
                    INTERNAL_RES,
                    OPEN_WOUND_RES
            );
        }
        return HEALTH_SCANNER_CONDITIONS;
    }

    public static List<BodyCondition> eyesightConditions() {
        if (EYESIGHT_CONDITIONS == null) {
            EYESIGHT_CONDITIONS = List.of(
                    SURGERY_INCISION,
                    CLAMPED_BLEEDING,
                    RETRACTED_SKIN,
                    DRILLED_BONES,

                    BURN,
                    OPEN_WOUND,
                    BLEED,
                    INFECTION,

                    BANDAGED,
                    BANDAGED_DIRTY,
                    OINMENTED,

                    DISLOCATION,
                    INTENSE_PAIN,
                    PLASTER_CAST,

                    ANALGESIA,
                    RESPIRATORY_ARREST,

                    TRAUMATIC_SHOCK,

                    BURN_RES,
                    INTERNAL_RES,
                    OPEN_WOUND_RES
            );
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
