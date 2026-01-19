
package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DontGetHurt.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("dgh_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.dgh"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.HEALTH_SCANNER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        for (var item : ModItems.ITEMS_SET) {
                            output.accept(item.get());
                        }
                        for (var item : ModItems.BLOCK_ITEMS_SET) {
                            output.accept(item.get());
                        }
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
