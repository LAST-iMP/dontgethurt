package com.lastimp.dgh.neoforge.data;

import com.lastimp.dgh.common.entry.register.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput output, Set<ResourceKey<LootTable>> requiredTables, List<SubProviderEntry> subProviders, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, requiredTables, subProviders, registries);
    }

    public static class ModBlockLootProvider extends BlockLootSubProvider {

        public static final Set<Block> BLOCK = Set.of(
                ModBlocks.OPERATING_BED_BLOCK.get()
        );

        public ModBlockLootProvider(HolderLookup.Provider provider) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), provider);
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
