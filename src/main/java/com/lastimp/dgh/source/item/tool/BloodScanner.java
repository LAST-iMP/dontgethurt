
package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.bloodConditions;

public class BloodScanner extends AbstractHealingItem {
    public BloodScanner(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide())
            HealthCapability.getAndApply(player, h -> BloodScanner.scanHealth(player, h, player.getScoreboardName()));
        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide()) {
            if (!HealthCapability.has(target)) {
                player.displayClientMessage(Component.translatable(target.getName().getString()).append("的血液很正常"), true);
            } else {
                var name = target instanceof Player ? target.getScoreboardName() : target.getName().getString();
                HealthCapability.getAndApply(target, h -> BloodScanner.scanHealth(player, h, name));
            }
        }
        return InteractionResult.SUCCESS;
    }

    public static void scanHealth(Player player, HealthCapability health, String name) {
        Blood blood = (Blood) health.getComponent(BodyComponents.BLOOD);
        boolean hasAbnormal = false;
        for (var condition : bloodConditions) {
            float value = blood.getConditionValue(condition);
            if (blood.abnormal(condition)) {
                if (!hasAbnormal)
                    player.displayClientMessage(Component.translatable(name).append("的血液状态为："), false);
                hasAbnormal = true;
                player.displayClientMessage(
                        Component.translatable(condition.toString()).append(Component.literal(": " + String.format("%.2f", value))),
                        false
                );
            }
        }
        if (!hasAbnormal) {
            player.displayClientMessage(Component.translatable(name).append("的血液很正常"), true);
        }
    }
}
