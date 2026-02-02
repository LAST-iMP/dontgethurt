
package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.source.core.menu.MenuOpenWrapper;
import com.lastimp.dgh.source.core.capability.HealthCapability;
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

public class HealthScanner extends AbstractHealingItem {
    private static List<ResourceLocation> HEALTH_SCANNER_CONDITIONS;
    private static List<ResourceLocation> EYESIGHT_CONDITIONS;

    public HealthScanner(Properties properties) {
        super(properties);
    }

    public static List<ResourceLocation> healthScannerConditions() {
        if (HEALTH_SCANNER_CONDITIONS == null) {
            HEALTH_SCANNER_CONDITIONS = new LinkedList<>();
            HEALTH_SCANNER_CONDITIONS.addAll(ConditionAccessor.injuryConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(ConditionAccessor.surgeryConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(ConditionAccessor.painConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(ConditionAccessor.comfortConditions);
            HEALTH_SCANNER_CONDITIONS.addAll(ConditionAccessor.resistConditions);
        }
        return HEALTH_SCANNER_CONDITIONS;
    }

    public static List<ResourceLocation> eyesightConditions() {
        healthScannerConditions();
        if (EYESIGHT_CONDITIONS == null) {
            EYESIGHT_CONDITIONS = new LinkedList<>();
            for (var condition : HEALTH_SCANNER_CONDITIONS) {
                if (ConditionAccessor.eyeVisible.contains(condition))
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
            MenuOpenWrapper.openHealthMenu(player, player.getUUID(), true);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!HealthCapability.has(target)) {
            return InteractionResult.SUCCESS;
        }
        if (!player.level().isClientSide) {
            MenuOpenWrapper.openHealthMenu(player, target.getUUID(), true);
        }
        return InteractionResult.CONSUME;
    }


}
