
package com.lastimp.dgh.source.register;

import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.item.limbs.HumanHand;
import com.lastimp.dgh.source.item.limbs.HumanLeg;
import com.lastimp.dgh.source.item.medicine.*;
import com.lastimp.dgh.source.item.tool.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.MODID;
import static com.lastimp.dgh.source.register.ModBlocks.OPERATING_BED_BLOCK;

public class ModItems {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final HashSet<DeferredItem<?>> ITEMS_SET = new HashSet<>();

    public static final DeferredItem<BlockItem> OPERATING_BED_BLOCK_ITEM = registerSimpleBlockItem(
            "operating_bed",
            OPERATING_BED_BLOCK,
            new Item.Properties()
                    .stacksTo(1)
    );

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

    public static final DeferredItem<LimbRefBeg> LIMB_REF_BEG = registerItem(
            "limb_ref_beg",
            LimbRefBeg::new,
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

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_WOOD = registerItem(
            "bone_implants_wood",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_WOOD),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_STONE = registerItem(
            "bone_implants_stone",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_STONE),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_COPPER = registerItem(
            "bone_implants_copper",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_COPPER),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_IRON = registerItem(
            "bone_implants_iron",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_IRON),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_GOLD = registerItem(
            "bone_implants_gold",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_GOLD),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_DIMOND = registerItem(
            "bone_implants_dimond",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<BoneImplants> BONE_IMPLANTS_NETHERITE = registerItem(
            "bone_implants_netherite",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final DeferredItem<SurgerySaw> SURGERY_SAW = registerItem(
            "surgery_saw",
            SurgerySaw::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final DeferredItem<SurgeryBones> BONE_NATURAL = registerItem(
            "bone_natural",
            (properties) -> new SurgeryBones(properties, null),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_WOOD = registerItem(
            "bone_wood",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_WOOD),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_STONE = registerItem(
            "bone_stone",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_STONE),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_COPPER = registerItem(
            "bone_copper",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_COPPER),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_IRON = registerItem(
            "bone_iron",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_IRON),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_GOLD = registerItem(
            "bone_gold",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_GOLD),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_DIMOND = registerItem(
            "bone_dimond",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<SurgeryBones> BONE_NETHERITE = registerItem(
            "bone_netherite",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Naloxone> NALOXONE = registerItem(
            "naloxone",
            Naloxone::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<MedicalStent> MEDICAL_STENT = registerItem(
            "medical_stent",
            MedicalStent::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Tourniquet> TOURNIQUET = registerItem(
            "tourniquet",
            Tourniquet::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Needle> NEEDLE = registerItem(
            "needle",
            Needle::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Drainage> DRAINAGE = registerItem(
            "drainage",
            Drainage::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<Adrenaline> ADRENALINE = registerItem(
            "adrenaline",
            Adrenaline::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<OxygenMask> OXYGEN_MASK = registerItem(
            "oxygen_mask",
            OxygenMask::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(400)
    );

    public static final DeferredItem<AntibioticOintment> ANTIBIOTIC_OINTMENT = registerItem(
            "antibiotic_ointment",
            AntibioticOintment::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(4)
    );

    public static final DeferredItem<AntisepticSprayer> ANTISEPTIC_SPRAYER = registerItem(
            "antiseptic_sprayer",
            AntisepticSprayer::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(10)
    );

    public static final DeferredItem<Item> ANTISEPTIC = registerItem(
            "antiseptic",
            Item::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static final DeferredItem<Autopulse> AUTOPULSE = registerItem(
            "autopulse",
            Autopulse::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(400)
    );

    public static final DeferredItem<Antibiotics> ANTIBIOTICS = registerItem(
            "antibiotics",
            Antibiotics::new,
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final DeferredItem<HumanHand> HUMAN_HAND = registerItem(
            "human_hand",
            HumanHand::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<HumanLeg> HUMAN_LEG = registerItem(
            "human_leg",
            HumanLeg::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<PlasticSkin> PLASTIC_SKIN = registerItem(
            "plastic_skin",
            PlasticSkin::new,
            new Item.Properties()
                    .stacksTo(64)
    );

    public static final DeferredItem<AntibioticGlue> ANTIBIOTIC_GLUE = registerItem(
            "antibiotic_glue",
            AntibioticGlue::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(4)
    );

    public static final DeferredItem<StasisBag> STASIS_BAG = registerItem(
            "stasis_bag",
            StasisBag::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(400)
    );

    public static final DeferredItem<Item> WALKING_STICK = registerItem(
            "walking_stick",
            Item::new,
            new Item.Properties()
                    .stacksTo(2)
    );

    public static final DeferredItem<StretcherItem> STRETCHER = registerItem(
            "stretcher",
            StretcherItem::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final DeferredItem<Mannitol> MANNITOL = registerItem(
            "mannitol",
            Mannitol::new,
            new Item.Properties()
                    .stacksTo(16)
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
