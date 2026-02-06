
package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

public class ModCreativeModTabs {
    public static final IEntry<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = PlatformService.REGISTRY_HANDLER.registerCreativeTabs("dgh_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.dgh"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.HEALTH_SCANNER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        for (var item : ModItems.ITEMS_SET) {
                            output.accept(item.get());
                        }
                    })
                    .build()
    );

    public static void register() {
    }
}
