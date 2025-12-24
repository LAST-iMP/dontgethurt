package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventBus {
    @SubscribeEvent
    public static void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        var player = event.getEntity();

        GameRules rules = event.getEntity().level().getGameRules();
        if(player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            rules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, event.getEntity().level().getServer());
        }

        var data = player.getPersistentData();
        var persistedTag = data.getCompound(Player.PERSISTED_NBT_TAG);
        var key = "dgh_new_player";
        if (!persistedTag.getBoolean(key)) {
            player.getInventory().add(new ItemStack(ModItems.HEALTH_CARE_BAG.get()));
            player.getInventory().add(new ItemStack(ModItems.BANDAGE.get(), 8));
            player.getInventory().add(new ItemStack(ModItems.MORPHINE.get(), 2));
            persistedTag.putBoolean(key, true);
            data.put(Player.PERSISTED_NBT_TAG, persistedTag);
        }
    }
}