
package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.api.healingItems.AbstractHealingItem;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class BloodScanner extends AbstractHealingItem {
    private static List<ResourceLocation> BLOOD_SCANNER_CONDITIONS;

    public BloodScanner(Properties properties) {
        super(properties);
    }

    public static List<ResourceLocation> bloodScannerConditions() {
        if (BLOOD_SCANNER_CONDITIONS == null) {
            BLOOD_SCANNER_CONDITIONS = List.of(
                    SEPSIS,
                    HEMOTRANSFUSION,
                    BLOOD_LOSS,
                    BLOOD_PRESSURE,
                    PH_LEVEL,
                    IMMUNITY,

                    OPIATE_OVERDOSE,
                    OXYGEN
            );
        }
        return BLOOD_SCANNER_CONDITIONS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (!level.isClientSide)
            BloodScanner.scanHealth(player, PlayerHealthCapability.get(player), player.getScoreboardName());
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
            Network.SERVER_INSTANCE.sendToServer(MyReadAllConditionData.getInstance(
                            target.getUUID(), null, OperationType.BLOOD_SCANN
                    ));
        }
    }

    public static void scanHealth(Player player, PlayerHealthCapability health, String name) {
        PlayerBlood blood = (PlayerBlood) health.getComponent(BodyComponents.BLOOD);
        boolean hasAbnormal = false;
        for (var condition : BloodScanner.bloodScannerConditions()) {
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
