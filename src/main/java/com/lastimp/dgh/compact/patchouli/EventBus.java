package com.lastimp.dgh.compact.patchouli;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.neoforge.Common;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import vazkii.patchouli.api.PatchouliAPI;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID)
public class EventBus {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!ModList.get().isLoaded("Patchouli")) return;

        var player = event.getEntity();

        var data = player.getPersistentData();
        var key = "dgh_has_book";

        if (!data.getBoolean(key)) {
            ItemStack book = PatchouliAPI.get().getBookStack(Common.ResourceLocation(DontGetHurt.MODID, "medical_guide"));
            player.getInventory().add(book);
            data.putBoolean(key, true);
        }
    }
}
