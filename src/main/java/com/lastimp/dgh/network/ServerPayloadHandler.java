package com.lastimp.dgh.network;

import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.KeyPressedType;
import com.lastimp.dgh.api.enums.OperationType;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.network.message.MyHealingItemUseData;
import com.lastimp.dgh.network.message.MyKeyPressedData;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.core.dyingSystem.PlayerDyingHandler;
import com.lastimp.dgh.source.core.healingSystem.BagHealerHandler;
import com.lastimp.dgh.source.core.menu.HealthMenu;
import com.lastimp.dgh.source.core.menu.MenuOpenWrapper;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.Utils;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.healingSystem.HealingHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class ServerPayloadHandler {

    public static void handleReadAllConditionData(final MyReadAllConditionData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            UUID uuid = new UUID(data.id_most(), data.id_least());
            ServerPlayer sender = context.getSender();
            var target = Utils.getLivingWithHealth(sender.serverLevel(), uuid);
            if (target == null) return;

            HealthCapability.getAndApply(target, health -> Network.CLIENT_INSTANCE.send(
                    PacketDistributor.PLAYER.with(context::getSender),
                    MyReadAllConditionData.getInstance(uuid, target.getId(), health.serializeNBT(), OperationType.valueOf(data.oper()))
            ));
        });
        context.setPacketHandled(true);
    }

    public static void handleClientPress(final MyKeyPressedData data, final Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            KeyPressedType key = KeyPressedType.valueOf(data.key());
            ServerPlayer player = ctx.get().getSender();
            switch (key) {
                case KEY_HEALTH_MENU:
                    HealthMenuProvider.open(player, player.getUUID(), false);
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
        });
        context.setPacketHandled(true);
    }

    public static void handleHealingItemUsageData(final MyHealingItemUseData data, Supplier<NetworkEvent.Context> ctx) {
        var context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sourcePlayer = ctx.get().getSender();
            if (sourcePlayer == null || !(sourcePlayer.containerMenu instanceof HealthMenu healthMenu)) return;
            var target = Utils.getLivingWithHealth(ctx.get().getSender().serverLevel(), new UUID(data.id_most(), data.id_least()));
            if (target == null) return;

            if (data.slotNum() == MyHealingItemUseData.HAND_PULSE) {
                HealthCapability.handPulse(target);
                return;
            }

            ItemStack stack = healthMenu.getStackBySlotNum(data.slotNum());
            if (stack.is(ModTags.MEDICAL_TOOLS_SMALL_BAGS) && !stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
                healthMenu.openBag(stack);
            } else {
                BodyComponents component = data.component().equals("NONE") ? null : BodyComponents.valueOf(data.component());
                if (stack.is(ModTags.MEDICAL_USAGE_BAGS)) {
                    BagHealerHandler.handleBagHealing(stack, sourcePlayer, target, component);
                } else {
                    HealingHandler.useItemOn(stack, sourcePlayer, target, component);
                }
                healthMenu.getSlot(data.slotNum()).setByPlayer(stack);
            }
        });
        context.setPacketHandled(true);
    }
}
