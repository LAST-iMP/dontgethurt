
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.lastimp.dgh.api.enums.OperationType.SYN;


@EventBusSubscriber(modid = DontGetHurt.MODID)
public class HealingEventHandler {
    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        GameRules rules = event.getEntity().level().getGameRules();
        if(event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.getEntity().level().getServer());
        }
    }

    @SubscribeEvent
    public static void onHealingUpdate(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;

        ServerPlayer player = (ServerPlayer) event.getEntity();
        var health = PlayerHealthCapability.getAndSet(player, h -> {
            h = h.update(player);
            updatePlayerHealth(h, player);
            return h;
        });
        PacketDistributor.sendToPlayer(player, MyReadAllConditionData.getInstance(
                player.getUUID(), health, SYN, player.registryAccess()
        ));
    }

    private static void updatePlayerHealth(PlayerHealthCapability health, ServerPlayer player) {
        float maxHealth = player.getMaxHealth() * health.playerVitality();
        if (player.isDeadOrDying()) {
            player.setHealth(0);
        } else if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative()) {
            player.setHealth(player.getMaxHealth());
        } else if (maxHealth > 0) {
            if ((int)maxHealth != (int)player.getHealth())
                player.setHealth(maxHealth);
        } else if (maxHealth > -player.getMaxHealth() && player.getServer().getPlayerList().getPlayers().size() > 1) {
            player.setHealth(0.01f);
        } else {
            player.setHealth(0);
        }
    }
}