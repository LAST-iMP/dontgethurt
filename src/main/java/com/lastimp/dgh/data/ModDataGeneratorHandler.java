
package com.lastimp.dgh.data;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
                (DataProvider.Factory<ModRecipeProvider>) ModRecipeProvider::new
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemTagsProvider>) output -> new ModItemTagsProvider(output, lp, DontGetHurt.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModBlockTagsProvider>) output -> new ModBlockTagsProvider(output, lp, DontGetHurt.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModDamageTypeTagsProvider>) output -> new ModDamageTypeTagsProvider(output, lp, DontGetHurt.MODID, efh)
        );

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModLootTableProvider>) output -> new ModLootTableProvider(
                        output, Collections.emptySet(), List.of(
                        new LootTableProvider.SubProviderEntry(ModLootTableProvider.ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                ))
        );

        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModSoundsProvider>) output -> new ModSoundsProvider(output, DontGetHurt.MODID, efh)
        );
    }
}
