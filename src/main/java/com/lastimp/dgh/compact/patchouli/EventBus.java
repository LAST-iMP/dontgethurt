package com.lastimp.dgh.compact.patchouli;

import com.lastimp.dgh.common.utils.ResourceHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import vazkii.patchouli.api.PatchouliAPI;
import com.lastimp.dgh.common.utils.Utils;

@EventBusSubscriber(modid = Utils.MODID)
public class EventBus {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!ModList.get().isLoaded("patchouli")) return;

        var player = event.getEntity();

        var data = player.getPersistentData().getCompound(Player.PERSISTED_NBT_TAG);
        var key = "dgh_has_book";

        if (!data.getBoolean(key)) {
            ItemStack book = PatchouliAPI.get().getBookStack(ResourceHelper.ModResource("medical_guide"));
            player.getInventory().add(book);
            data.putBoolean(key, true);
        }
    }
}
