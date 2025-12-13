
package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.bodyPart.Blood;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.bloodConditions;

public class BloodScanner extends AbstractHealingItem {
    public BloodScanner(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide)
            BloodScanner.scanHealth(player, HealthCapability.get(player), player.getScoreboardName());
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide) {
            this.scanEntity(player, target);
        }
        return InteractionResult.SUCCESS;
    }

    private void scanEntity(Player player, LivingEntity entity) {
        if (!(entity instanceof Player target)) {
            player.sendSystemMessage(Component.literal(entity.getName().getString() + "的血液很正常"));
        } else {
            PacketDistributor.sendToServer(MyReadAllConditionData.getInstance(
                    target.getUUID(), null, OperationType.BLOOD_SCANN, Minecraft.getInstance().player.registryAccess()
            ));
        }
    }

    public static void scanHealth(Player player, HealthCapability health, String name) {
        Blood blood = (Blood) health.getComponent(BodyComponents.BLOOD);
        boolean hasAbnormal = false;
        for (var condition : bloodConditions) {
            float value = blood.getConditionValue(condition);
            if (blood.abnormal(condition)) {
                if (!hasAbnormal)
                    player.sendSystemMessage(Component.literal(name + "的血液状态为："));
                hasAbnormal = true;
                player.sendSystemMessage(
                        Component.literal(Component.translatable(condition.toString()).getString() + ": " + String.format("%.2f", value))
                );
            }
        }
        if (!hasAbnormal) {
            player.sendSystemMessage(Component.literal(name + "的血液状态正常"));
        }
    }
}
