/*
* MIT License

Copyright (c) 2023 NeoForged project

This license applies to the template files as supplied by github.com/NeoForged/MDK


Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.lastimp.dgh.source.Register;

import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.item.*;
import com.lastimp.dgh.source.item.BloodScanner;
import com.lastimp.dgh.source.item.HealthScanner;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.lastimp.dgh.DontGetHurt.MODID;

@EventBusSubscriber(modid = MODID)
public class ModItems {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredBlock<Block> OPERATING_BED_BLOCK = BLOCKS.registerBlock(
            "operating_bed",
            OperatingBedBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_BED)
    );

    public static final DeferredItem<BlockItem> OPERATING_BED_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "operating_bed",
            OPERATING_BED_BLOCK,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Item> HEALTH_SCANNER = ITEMS.registerItem(
            "health_scanner",
            HealthScanner::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Item> BLOOD_PACK = ITEMS.registerItem(
            "blood_pack",
            BloodPacks::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Item> BLOOD_PACK_EMPTY = ITEMS.registerItem(
            "blood_pack_empty",
            BloodPacksEmpty::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<BloodScanner> BLOOD_SCANNER = ITEMS.registerItem(
            "blood_scanner",
            BloodScanner::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Bandages> BANDAGE = ITEMS.registerItem(
            "bandage",
            Bandages::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static final DeferredItem<Morphine> MORPHINE = ITEMS.registerItem(
            "morphine",
            Morphine::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Gypsum> GYPSUM = ITEMS.registerItem(
            "gypsum",
            Gypsum::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Sutures> SUTURE = ITEMS.registerItem(
            "suture",
            Sutures::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(OPERATING_BED_BLOCK_ITEM);
    }
}
