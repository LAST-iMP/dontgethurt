
package com.lastimp.dgh.source.Register;

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

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("dgh_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.dgh"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.HEALTH_SCANNER.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.HEALTH_SCANNER.get());
                        output.accept(ModItems.BLOOD_SCANNER.get());
                        output.accept(ModItems.OPERATING_BED_BLOCK_ITEM.get());
                        output.accept(ModItems.BLOOD_PACK.get());
                        output.accept(ModItems.BLOOD_PACK_EMPTY.get());
                        output.accept(ModItems.BANDAGE.get());
                        output.accept(ModItems.GYPSUM.get());
                        output.accept(ModItems.MORPHINE.get());
                        output.accept(ModItems.SUTURE.get());
                        output.accept(ModItems.HEALTH_CARE_BAG.get());
                        output.accept(ModItems.WOOD_WRENCH.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
