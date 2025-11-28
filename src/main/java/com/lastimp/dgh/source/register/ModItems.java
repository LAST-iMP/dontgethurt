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

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DontGetHurt.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DontGetHurt.MODID);

    public static final HashSet<RegistryObject<?>> ITEMS_SET = new HashSet<>();

//    public static final RegistryObject<Block> OPERATING_BED_BLOCK = BLOCKS.register(
//            "operating_bed",
//            () -> new OperatingBedBlock(
//                    BlockBehaviour.Properties.copy(Blocks.RED_BED.defaultBlockState().getBlock())
//            )
//    );
//
//    public static final RegistryObject<BlockItem> OPERATING_BED_BLOCK_ITEM = ITEMS.register(
//            "operating_bed",
//            () -> new OperatingBedItem(
//                    OPERATING_BED_BLOCK.get(),
//                    new Item.Properties()
//                            .stacksTo(1)
//            )
//    );

    public static final RegistryObject<Item> HEALTH_SCANNER = registerItem(
            "health_scanner",
            () -> new HealthScanner(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> BLOOD_PACK = registerItem(
            "blood_pack",
            () -> new BloodPacks(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Item> BLOOD_PACK_EMPTY = registerItem(
            "blood_pack_empty",
            () -> new BloodPacksEmpty(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<BloodScanner> BLOOD_SCANNER = registerItem(
            "blood_scanner",
            () -> new BloodScanner(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Bandages> BANDAGE = registerItem(
            "bandage",
            () -> new Bandages(
                    new Item.Properties()
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<Morphine> MORPHINE = registerItem(
            "morphine",
            () -> new Morphine(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Gypsum> GYPSUM = registerItem(
            "gypsum",
            () -> new Gypsum(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Sutures> SUTURE = registerItem(
            "suture",
            () -> new Sutures(
                    new Item.Properties()
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<HealthCareBag> HEALTH_CARE_BAG = registerItem(
            "health_care_bag",
            () -> new HealthCareBag(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<WoodWrench> WOOD_WRENCH = registerItem(
            "wood_wrench",
            () -> new WoodWrench(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }

    private static <T extends Item> RegistryObject<T> registerItem(final String name, final Supplier<T> sup) {
        RegistryObject<T> item = ITEMS.register(name, sup);
        ITEMS_SET.add(item);
        return item;
    }

//    private static RegistryObject<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
//        RegistryObject<BlockItem> blockItem = ITEMS.register(name, block, properties);
//        ITEMS_SET.add(blockItem);
//        return blockItem;
//    }

//    @SubscribeEvent
//    public static void addCreative(BuildCreativeModeTabContentsEvent event)
//    {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
//            event.accept(HEALTH_SCANNER);
//    }
}
