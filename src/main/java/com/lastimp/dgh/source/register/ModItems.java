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

package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.item.*;
import com.lastimp.dgh.source.item.BloodScanner;
import com.lastimp.dgh.source.item.HealthScanner;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.lastimp.dgh.DontGetHurt.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DontGetHurt.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DontGetHurt.MODID);

    public static final RegistryObject<Block> OPERATING_BED_BLOCK = BLOCKS.register(
            "operating_bed",
            () -> new OperatingBedBlock(
                    BlockBehaviour.Properties.copy(Blocks.RED_BED.defaultBlockState().getBlock())
            )
    );

    public static final RegistryObject<BlockItem> OPERATING_BED_BLOCK_ITEM = ITEMS.register(
            "operating_bed",
            () -> new OperatingBedItem(
                    OPERATING_BED_BLOCK.get(),
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> HEALTH_SCANNER = ITEMS.register(
            "health_scanner",
            () -> new HealthScanner(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> BLOOD_PACK = ITEMS.register(
            "blood_pack",
            () -> new BloodPacks(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Item> BLOOD_PACK_EMPTY = ITEMS.register(
            "blood_pack_empty",
            () -> new BloodPacksEmpty(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<BloodScanner> BLOOD_SCANNER = ITEMS.register(
            "blood_scanner",
            () -> new BloodScanner(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Bandages> BANDAGE = ITEMS.register(
            "bandage",
            () -> new Bandages(
                    new Item.Properties()
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<Morphine> MORPHINE = ITEMS.register(
            "morphine",
            () -> new Morphine(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Gypsum> PLASTER = ITEMS.register(
            "gypsum",
            () -> new Gypsum(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Sutures> SUTURE = ITEMS.register(
            "suture",
            () -> new Sutures(
                    new Item.Properties()
                            .stacksTo(64)
            )
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
