
package com.lastimp.dgh.source.register;

import com.lastimp.dgh.source.item.tool.*;
import com.lastimp.dgh.source.item.medicine.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.MODID;

public class ModItems {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final HashSet<DeferredItem<?>> ITEMS_SET = new HashSet<>();

//    public static final DeferredBlock<Block> OPERATING_BED_BLOCK = BLOCKS.registerBlock(
//            "operating_bed",
//            OperatingBedBlock::new,
//            BlockBehaviour.Properties.ofFullCopy(Blocks.RED_BED)
//    );

//    public static final DeferredItem<BlockItem> OPERATING_BED_BLOCK_ITEM = registerSimpleBlockItem(
//            "operating_bed",
//            OPERATING_BED_BLOCK,
//            new Item.Properties()
//                    .stacksTo(1)
//    );

    public static final DeferredItem<Item> HEALTH_SCANNER = registerItem(
            "health_scanner",
            HealthScanner::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Item> BLOOD_PACK = registerItem(
            "blood_pack",
            BloodPacks::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Item> BLOOD_PACK_EMPTY = registerItem(
            "blood_pack_empty",
            BloodPacksEmpty::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<BloodScanner> BLOOD_SCANNER = registerItem(
            "blood_scanner",
            BloodScanner::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Bandages> BANDAGE = registerItem(
            "bandage",
            Bandages::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static final DeferredItem<Morphine> MORPHINE = registerItem(
            "morphine",
            Morphine::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Gypsum> GYPSUM = registerItem(
            "gypsum",
            Gypsum::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Sutures> SUTURE = registerItem(
            "suture",
            Sutures::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static final DeferredItem<HealthCareBag> HEALTH_CARE_BAG = registerItem(
            "health_care_bag",
            HealthCareBag::new,
            new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

    public static final DeferredItem<SurgeryToolBag> SURGERY_TOOL_BAG = registerItem(
            "surgery_tool_bag",
            SurgeryToolBag::new,
            new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

    public static final DeferredItem<WoodWrench> WOOD_WRENCH = registerItem(
            "wood_wrench",
            WoodWrench::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(60)
    );

    public static final DeferredItem<Scalpel> SCALPEL = registerItem(
            "scalpel",
            Scalpel::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final DeferredItem<Hemostat> HEMOSTAT = registerItem(
            "hemostat",
            Hemostat::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)

    );

    public static final DeferredItem<Retractor> RETRACTOR = registerItem(
            "retractor",
            Retractor::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final DeferredItem<SurgicalDrill> SURGICAL_DRILL = registerItem(
            "surgical_drill",
            SurgicalDrill::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final DeferredItem<Tweezer> TWEEZER = registerItem(
            "tweezer",
            Tweezer::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS = registerItem(
            "bone_implants",
            BoneImplants::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    private static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, ? extends T> func, Item.Properties properties) {
        DeferredItem<T> item = ITEMS.registerItem(name, func, properties);
        ITEMS_SET.add(item);
        return item;
    }

    private static DeferredItem<BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
        DeferredItem<BlockItem> blockItem = ITEMS.registerSimpleBlockItem(name, block, properties);
        ITEMS_SET.add(blockItem);
        return blockItem;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
