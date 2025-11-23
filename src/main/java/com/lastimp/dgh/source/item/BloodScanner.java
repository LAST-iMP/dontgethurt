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
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.source.core.bodyPart.PlayerBlood;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.api.enums.BodyCondition.*;

public class BloodScanner extends Item {
    private static List<BodyCondition> BLOOD_SCANNER_CONDITIONS;

    public BloodScanner(Properties properties) {
        super(properties);
    }

    public static List<BodyCondition> bloodScannerConditions() {
        if (BLOOD_SCANNER_CONDITIONS == null) {
            BLOOD_SCANNER_CONDITIONS = List.of(
                    BLOOD_VOLUME,
                    SEPSIS,
                    HEMOTRANSFUSION,
                    BLOOD_LOSS,
                    BLOOD_PRESSURE,
                    PH_LEVEL,
                    IMMUNITY,

                    OPIATE_OVERDOSE,
                    OPIATE_ADDICTED
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
            PacketDistributor.sendToServer(MyReadAllConditionData.getInstance(
                    target.getUUID(), null, OperationType.BLOOD_SCANN, Minecraft.getInstance().player.registryAccess()
            ));
        }
    }

    public static void scanHealth(Player player, PlayerHealthCapability health, String name) {
        PlayerBlood blood = (PlayerBlood) health.getComponent(BodyComponents.BLOOD);
        boolean hasAbnormal = false;
        for (BodyCondition condition : BloodScanner.bloodScannerConditions()) {
            float value = blood.getConditionValue(condition);
            if (condition.abnormal(value)) {
                hasAbnormal = true;
                player.sendSystemMessage(
                        Component.literal(condition + ": " + String.format("%.2f", value))
                );
            }
        }
        if (!hasAbnormal) {
            player.sendSystemMessage(Component.literal(name + "的血液状态正常"));
        }
    }
}
