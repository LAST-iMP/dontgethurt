package com.lastimp.dgh.data;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var efh = event.getExistingFileHelper();

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModLanguageProvider>) output -> new ModLanguageProvider(output, DontGetHurt.MODID, "zh_cn")
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemModelProvider>) output -> new ModItemModelProvider(output, DontGetHurt.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModBlockStateProvider>) output -> new ModBlockStateProvider(output, DontGetHurt.MODID, efh)
        );

        var lp = event.getLookupProvider();
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModRecipeProvider>) output -> new ModRecipeProvider(output, lp)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModTagsProvider>) output -> new ModTagsProvider(output, lp, DontGetHurt.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModLootTableProvider>) output -> new ModLootTableProvider(
                        output, Collections.emptySet(), List.of(
                                new LootTableProvider.SubProviderEntry(ModLootTableProvider.ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                ), lp)
        );
    }
}
