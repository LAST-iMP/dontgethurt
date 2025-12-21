package com.lastimp.dgh.source.core.player;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID)
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
        var key = "dgh_new_player";
        if (!data.getBoolean(key)) {
            player.getInventory().add(new ItemStack(ModItems.HEALTH_CARE_BAG.get()));
            player.getInventory().add(new ItemStack(ModItems.BANDAGE.get(), 8));
            player.getInventory().add(new ItemStack(ModItems.MORPHINE.get(), 2));
            data.putBoolean(key, true);
        }
    }
}