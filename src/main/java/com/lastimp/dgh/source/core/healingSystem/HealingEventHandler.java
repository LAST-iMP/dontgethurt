
package com.lastimp.dgh.source.core.healingSystem;

import com.lastimp.dgh.Config;
import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.network.message.MyReadAllConditionData;
import com.lastimp.dgh.network.message.Network;
import com.lastimp.dgh.source.core.player.PlayerDyingHandler;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static com.lastimp.dgh.api.enums.OperationType.SYN;


@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HealingEventHandler {
    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        GameRules rules = event.getEntity().level().getGameRules();
        if(event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.getEntity().level().getServer());
        }
    }

    @SubscribeEvent
    public static void onHealthUpdate(TickEvent.PlayerTickEvent event) {
        if (event.side.isClient()) return;

        ServerPlayer player = (ServerPlayer) event.player;
        var health = HealthCapability.getAndSet(player, h -> {
            h = h.update(player);
            if (!checkTotemDeathProtection(h, player))
                updatePlayerHealth(h, player);
            return h;
        });
        Network.CLIENT_INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                MyReadAllConditionData.getInstance(player.getUUID(), health, SYN)
        );
    }

    private static void updatePlayerHealth(HealthCapability health, ServerPlayer player) {
        float maxHealth = player.getMaxHealth() * health.playerVitality();
        if (player.isDeadOrDying()) {
            PlayerDyingHandler.setDead(player);
        } else if (player.level().getDifficulty() == Difficulty.PEACEFUL || player.gameMode.isCreative()) {
            player.setHealth(player.getMaxHealth());
        } else if (maxHealth > 0) {
            if ((int)maxHealth != (int)player.getHealth())
                player.setHealth(maxHealth);
        } else if (maxHealth > -player.getMaxHealth() && player.getServer().getPlayerList().getPlayers().size() > 1) {
            player.setHealth(0.01f);
        } else {
            PlayerDyingHandler.setDead(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerHealing(LivingHealEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getAmount() < 0.01) return;

        float amount = event.getAmount() / (player.getMaxHealth() * Config.body_life_factor);
        HealingHandler.handleValindaHealing(player, amount * Config.healing_factor);
    }

    private static boolean checkTotemDeathProtection(HealthCapability health, ServerPlayer player) {
        if (health.playerVitality() > 0) return false;

        ItemStack itemstack = null;
        for (InteractionHand interactionhand : InteractionHand.values()) {
            ItemStack itemstack1 = player.getItemInHand(interactionhand);
            DamageSource source = player.level().damageSources().genericKill();
            if (itemstack1.is(Items.TOTEM_OF_UNDYING) && ForgeHooks.onLivingUseTotem(player, source, itemstack1, interactionhand)) {
                itemstack = itemstack1.copy();
                itemstack1.shrink(1);
                break;
            }
        }
        if (itemstack == null) return false;

        player.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
        CriteriaTriggers.USED_TOTEM.trigger(player, itemstack);
        player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);

        HealingHandler.handleValindaHealing(player, player.getMaxHealth());
        player.setHealth(1);
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
        player.level().broadcastEntityEvent(player, (byte)35);
        return true;
    }
}
