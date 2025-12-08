package com.lastimp.dgh.data;

import com.lastimp.dgh.source.register.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, Set<ResourceLocation> requiredTables, List<SubProviderEntry> subProviders) {
        super(output, requiredTables, subProviders);
    }

    public static class ModBlockLootProvider extends BlockLootSubProvider {

        public static final Set<Block> BLOCK = Set.of(
                ModBlocks.OPERATING_BED_BLOCK.get()
        );

        protected ModBlockLootProvider(Set<Item> explosionResistant, FeatureFlagSet enabledFeatures) {
            super(explosionResistant, enabledFeatures);
        }

        public ModBlockLootProvider() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.dropSelf(ModBlocks.OPERATING_BED_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BLOCK;
        }
    }
}
