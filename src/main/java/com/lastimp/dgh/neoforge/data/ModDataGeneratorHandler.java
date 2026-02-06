
package com.lastimp.dgh.neoforge.data;

import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import com.lastimp.dgh.common.utils.Utils;

@EventBusSubscriber(modid = Utils.MODID)
public class ModDataGeneratorHandler {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var efh = event.getExistingFileHelper();

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModLanguageProvider>) output -> new ModLanguageProvider(output, Utils.MODID, "zh_cn")
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemModelProvider>) output -> new ModItemModelProvider(output, Utils.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModBlockStateProvider>) output -> new ModBlockStateProvider(output, Utils.MODID, efh)
        );

        var lp = event.getLookupProvider();
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModRecipeProvider>) output -> new ModRecipeProvider(output, lp)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemTagsProvider>) output -> new ModItemTagsProvider(output, lp, Utils.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModBlockTagsProvider>) output -> new ModBlockTagsProvider(output, lp, Utils.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModDamageTypeTagsProvider>) output -> new ModDamageTypeTagsProvider(output, lp, Utils.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModLootTableProvider>) output -> new ModLootTableProvider(
                        output, Collections.emptySet(), List.of(
                        new LootTableProvider.SubProviderEntry(ModLootTableProvider.ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                ), lp)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModSoundsProvider>) output -> new ModSoundsProvider(output, Utils.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModPoiTypeTagsProvider>) output -> new ModPoiTypeTagsProvider(
                        output, lp, Utils.MODID, efh
                )
        );
    }
}
