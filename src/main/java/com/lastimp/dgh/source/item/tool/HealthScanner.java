
package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class HealthScanner extends AbstractHealingItem {
    private static List<Identifier> HEALTH_SCANNER_CONDITIONS;
    private static List<Identifier> EYESIGHT_CONDITIONS;

    public HealthScanner(Properties properties) {
        super(properties);
    }

    public static List<Identifier> healthScannerConditions() {
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

    public static List<Identifier> eyesightConditions() {
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
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (usedHand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if (!level.isClientSide()) {
            HealthMenuProvider.open(player, player.getUUID(), true);
        }
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!HealthCapability.has(target)) {
            return InteractionResult.SUCCESS;
        }
        if (!player.level().isClientSide()) {
            HealthMenuProvider.open(player, target.getUUID(), true);
        }
        return InteractionResult.CONSUME;
    }


}
