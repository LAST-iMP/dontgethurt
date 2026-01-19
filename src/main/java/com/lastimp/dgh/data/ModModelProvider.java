
package com.lastimp.dgh.data;

import com.lastimp.dgh.source.register.ModBlocks;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output, String modid) {
        super(output, modid);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        var block = ModBlocks.OPERATING_BED_BLOCK.get();
        MultiVariant multivariant = BlockModelGenerators.plainVariant(ModelLocationUtils.decorateBlockModelLocation("bed"));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multivariant));

        for (var item : ModItems.ITEMS_SET) {
            itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
        }
        for (var item : ModItems.BLOCK_ITEMS_SET) {
            itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
