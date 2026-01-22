
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.dyingSystem.PlayerDyingHandler;
import com.lastimp.dgh.source.core.menu.BagMenu;
import com.lastimp.dgh.source.core.menu.HealthMenu;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.menu.MenuOpenWrapper;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ServerPayloadHandler {

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            UUID uuid = new UUID(data.id_most(), data.id_least());
            ServerPlayer sender = (ServerPlayer) context.player();
            var target = Utils.getLivingWithHealth(sender.serverLevel(), uuid);
            if (target == null) return;

            HealthCapability.getAndApply(target, health -> PacketDistributor.sendToPlayer(
                    (ServerPlayer) context.player(),
                    MyReadAllConditionData.getInstance(uuid, target.getId(), health.serializeNBT(sender.registryAccess()), OperationType.valueOf(data.oper()))
            ));
        })
        .exceptionally(e -> {
            context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
            return null;
        });
    }

    public static void handleClientPress(final MyKeyPressedData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    KeyPressedType key = KeyPressedType.valueOf(data.key());
                    ServerPlayer player = (ServerPlayer) context.player();
                    switch (key) {
                        case KEY_HEALTH_MENU:
                            MenuOpenWrapper.openHealthMenu(player, player.getUUID(), false);
                            break;
                        case KEY_SLOT_USE:
                            MenuOpenWrapper.openMenu(player.getInventory().getItem(data.index()), player);
                            break;
                        case GIVE_UP:
                            PlayerDyingHandler.setPlayerDead(player);
                            break;
                        case CALL_FOR_HELP:
                            player.getServer().getPlayerList().getPlayers().forEach(p -> {
                                p.sendSystemMessage(Component.literal(
                                        player.getScoreboardName() + "在("
                                                + String.format("%.1f", player.position().x) + ", "
                                                + String.format("%.1f", player.position().y) + ", "
                                                + String.format("%.1f", player.position().z) + ")需要救助"
                                ));
                            });
                            break;
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
                    return null;
                });

    }

    public static void handleHealingItemUsageData(final MyHealingItemUseData data, final IPayloadContext context) {
        context.enqueueWork(() -> {
                    ServerPlayer sourcePlayer = (ServerPlayer) context.player();
                    var target = Utils.getLivingWithHealth(((ServerPlayer) context.player()).serverLevel(), new UUID(data.id_most(), data.id_least()));
                    if (target == null) return;

                    if (data.slotNum() == MyHealingItemUseData.HAND_PULSE) {
                        HealthCapability.handPulse(target);
                        return;
                    }

                    if (sourcePlayer.containerMenu instanceof HealthMenu healthMenu) {
                        HealingHandler.handleHealthMenuItemUse(healthMenu, data, sourcePlayer, target);
                    } else if (sourcePlayer.containerMenu instanceof BagMenu.MedicineSmallBag medicineSmallBagMenu) {
                        HealingHandler.handleMedicineBagMenuItemUse(medicineSmallBagMenu, data, sourcePlayer, target);
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
                    return null;
                });
    }
}
