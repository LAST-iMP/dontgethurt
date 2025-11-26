
package com.lastimp.dgh.source.Register;

import com.lastimp.dgh.source.block.OperatingBedBlock;
import com.lastimp.dgh.source.item.*;
import com.lastimp.dgh.source.item.BloodScanner;
import com.lastimp.dgh.source.item.HealthScanner;
import com.lastimp.dgh.source.item.bases.BackpackInventory;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
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

    public static final DeferredItem<HealthCareBag> HEALTH_CARE_BAG = ITEMS.registerItem(
            "health_care_bag",
            HealthCareBag::new,
            new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

        public static final DeferredItem<WoodWrench> WOOD_WRENCH = ITEMS.registerItem(
            "wood_wrench",
            WoodWrench::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
