
package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.menu.HealthMenu;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.player.DyingHandler;
import com.lastimp.dgh.source.register.ModItems;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthCareBagMenuProvider;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.menu.menuProvider.SurgeryToolBagMenuProvider;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
                    HealthCapability health = HealthCapability.get(target);

                    PacketDistributor.sendToPlayer(
                            (ServerPlayer) context.player(),
                            MyReadAllConditionData.getInstance(uuid, health, OperationType.valueOf(data.oper()), context.player().registryAccess())
                    );
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
                            HealthMenuProvider.open(player, player.getUUID(), false);
                            break;
                        case KEY_SLOT_USE:
                            var slot = player.getInventory().getItem(data.index());
                            if (slot.is(ModItems.HEALTH_SCANNER))
                                HealthMenuProvider.open(player, player.getUUID(), true);
                            if (slot.is(ModItems.HEALTH_CARE_BAG))
                                HealthCareBagMenuProvider.open(player, slot);
                            if (slot.is(ModItems.SURGERY_TOOL_BAG))
                                SurgeryToolBagMenuProvider.open(player, slot);
                            break;
                        case GIVE_UP:
                            DyingHandler.setPlayerDead(player);
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
                    if (!(sourcePlayer.containerMenu instanceof HealthMenu healthMenu)) return;
                    ItemStack stack = healthMenu.getStackBySlotNum(data.slotNum());
                    if (stack.is(ModTags.MEDICAL_TOOLS_BAGS)) {
                        healthMenu.openBag(stack);
                    } else {
                        UUID targetID = new UUID(data.id_most(), data.id_least());
                        var target = Utils.getLivingWithHealth(((ServerPlayer) context.player()).serverLevel(), targetID);
                        if (target == null) return;
                        BodyComponents component = data.component().equals("NONE") ? null : BodyComponents.valueOf(data.component());
                        HealingHandler.useItemOn(stack, sourcePlayer, target, component);
                    }
                })
                .exceptionally(e -> {
                    context.disconnect(Component.translatable("dgh.networking.failed", e.getMessage()));
                    return null;
                });
    }
}
