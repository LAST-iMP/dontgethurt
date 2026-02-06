
package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.item.limbs.HumanHand;
import com.lastimp.dgh.common.item.limbs.HumanLeg;
import com.lastimp.dgh.common.item.medicine.*;
import com.lastimp.dgh.common.item.organs.*;
import com.lastimp.dgh.common.item.tool.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.common.entry.register.ModBlocks.OPERATING_BED_BLOCK;

public class ModItems {
    public static final HashSet<IEntry<Item, ? extends Item>> ITEMS_SET = new HashSet<>();

    public static final IEntry<Item, BlockItem> OPERATING_BED_BLOCK_ITEM = registerSimpleBlockItem(
            "operating_bed", OPERATING_BED_BLOCK,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Item> HEALTH_SCANNER = registerItem(
            "health_scanner", HealthScanner::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, BloodPacks> BLOOD_PACK = registerItem(
            "blood_pack", BloodPacks::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Item> BLOOD_PACK_EMPTY = registerItem(
            "blood_pack_empty", BloodPacksEmpty::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, BloodScanner> BLOOD_SCANNER = registerItem(
            "blood_scanner", BloodScanner::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Bandages> BANDAGE = registerItem(
            "bandage", Bandages::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, Morphine> MORPHINE = registerItem(
            "morphine", Morphine::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Gypsum> GYPSUM = registerItem(
            "gypsum", Gypsum::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Sutures> SUTURE = registerItem(
            "suture", Sutures::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, HealthCareBag> HEALTH_CARE_BAG = registerItem(
            "health_care_bag", HealthCareBag::new,
            new Item.Properties().stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

    public static final IEntry<Item, SurgeryToolBag> SURGERY_TOOL_BAG = registerItem(
            "surgery_tool_bag", SurgeryToolBag::new,
            new Item.Properties().stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

    public static final IEntry<Item, LimbRefBeg> LIMB_REF_BEG = registerItem(
            "limb_ref_beg", LimbRefBeg::new,
            new Item.Properties().stacksTo(1)
                    .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
    );

    public static final IEntry<Item, WoodWrench> WOOD_WRENCH = registerItem(
            "wood_wrench", WoodWrench::new,
            new Item.Properties().stacksTo(1).durability(60)
    );

    public static final IEntry<Item, Scalpel> SCALPEL = registerItem(
            "scalpel", Scalpel::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item, Hemostat> HEMOSTAT = registerItem(
            "hemostat", Hemostat::new,
            new Item.Properties().stacksTo(1).durability(300)

    );

    public static final IEntry<Item, Retractor> RETRACTOR = registerItem(
            "retractor", Retractor::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item, SurgicalDrill> SURGICAL_DRILL = registerItem(
            "surgical_drill", SurgicalDrill::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item, Tweezer> TWEEZER = registerItem(
            "tweezer", Tweezer::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS = registerItem(
            "bone_implants", BoneImplants::new,
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_WOOD = registerItem(
            "bone_implants_wood", (properties) -> new BoneImplants(properties, BodyCondition.BONE_WOOD),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_STONE = registerItem(
            "bone_implants_stone", (properties) -> new BoneImplants(properties, BodyCondition.BONE_STONE),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_COPPER = registerItem(
            "bone_implants_copper", (properties) -> new BoneImplants(properties, BodyCondition.BONE_COPPER),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_IRON = registerItem(
            "bone_implants_iron", (properties) -> new BoneImplants(properties, BodyCondition.BONE_IRON),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_GOLD = registerItem(
            "bone_implants_gold", (properties) -> new BoneImplants(properties, BodyCondition.BONE_GOLD),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_DIMOND = registerItem(
            "bone_implants_dimond", (properties) -> new BoneImplants(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, BoneImplants> BONE_IMPLANTS_NETHERITE = registerItem(
            "bone_implants_netherite", (properties) -> new BoneImplants(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item, SurgerySaw> SURGERY_SAW = registerItem(
            "surgery_saw", SurgerySaw::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item, SurgeryBones> BONE_NATURAL = registerItem(
            "bone_natural", (properties) -> new SurgeryBones(properties, null),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_WOOD = registerItem(
            "bone_wood", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_WOOD),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_STONE = registerItem(
            "bone_stone", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_STONE),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_COPPER = registerItem(
            "bone_copper", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_COPPER),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_IRON = registerItem(
            "bone_iron", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_IRON),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_GOLD = registerItem(
            "bone_gold", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_GOLD),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_DIMOND = registerItem(
            "bone_dimond", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, SurgeryBones> BONE_NETHERITE = registerItem(
            "bone_netherite", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Naloxone> NALOXONE = registerItem(
            "naloxone", Naloxone::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, MedicalStent> MEDICAL_STENT = registerItem(
            "medical_stent", MedicalStent::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Tourniquet> TOURNIQUET = registerItem(
            "tourniquet", Tourniquet::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Needle> NEEDLE = registerItem(
            "needle", Needle::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Drainage> DRAINAGE = registerItem(
            "drainage", Drainage::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Adrenaline> ADRENALINE = registerItem(
            "adrenaline", Adrenaline::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, OxygenMask> OXYGEN_MASK = registerItem(
            "oxygen_mask", OxygenMask::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item, AntibioticOintment> ANTIBIOTIC_OINTMENT = registerItem(
            "antibiotic_ointment", AntibioticOintment::new,
            new Item.Properties().stacksTo(1).durability(4).setNoRepair()
    );

    public static final IEntry<Item, AntisepticSprayer> ANTISEPTIC_SPRAYER = registerItem(
            "antiseptic_sprayer", AntisepticSprayer::new,
            new Item.Properties().stacksTo(1).durability(10)
    );

    public static final IEntry<Item, Item> ANTISEPTIC = registerItem(
            "antiseptic", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, Autopulse> AUTOPULSE = registerItem(
            "autopulse", Autopulse::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item, Antibiotics> ANTIBIOTICS = registerItem(
            "antibiotics", Antibiotics::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, HumanHand> HUMAN_HAND = registerItem(
            "human_hand", HumanHand::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, HumanLeg> HUMAN_LEG = registerItem(
            "human_leg", HumanLeg::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, PlasticSkin> PLASTIC_SKIN = registerItem(
            "plastic_skin", PlasticSkin::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, AntibioticGlue> ANTIBIOTIC_GLUE = registerItem(
            "antibiotic_glue", AntibioticGlue::new,
            new Item.Properties().stacksTo(1).durability(4)
    );

    public static final IEntry<Item, StasisBag> STASIS_BAG = registerItem(
            "stasis_bag", StasisBag::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item, Item> WALKING_STICK = registerItem(
            "walking_stick", Item::new,
            new Item.Properties().stacksTo(2)
    );

    public static final IEntry<Item, StretcherItem> STRETCHER = registerItem(
            "stretcher", StretcherItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Mannitol> MANNITOL = registerItem(
            "mannitol", Mannitol::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, AutoUseBag> AUTO_USE_BAG = registerItem(
            "auto_use_bag", AutoUseBag::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, MedicineBag> MEDICINE_BAG = registerItem(
            "medicine_bag", MedicineBag::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, HerbBandage> HERB_BANDAGE = registerItem(
            "herb_bandage", HerbBandage::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, Clamp> CLAMP = registerItem(
            "clamp", Clamp::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Item> GRASS_STRING = registerItem(
            "grass_string", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item, AED> AED = registerItem(
            "aed", AED::new,
            new Item.Properties().stacksTo(1).durability(20).setNoRepair()
    );

    public static final IEntry<Item, FoodConsumer> FOOD_CONSUMER = registerItem(
            "food_consumer", FoodConsumer::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Fentanyl> FENTANYL = registerItem(
            "fentanyl", Fentanyl::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Hyperzine> HYPERZINE = registerItem(
            "hyperzine", Hyperzine::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Hardener> HARDENER = registerItem(
            "hardener", Hardener::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item, Brain> BRAIN = registerItem(
            "brain", Brain::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Eye> EYE = registerItem(
            "eye", Eye::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Heart> HEART = registerItem(
            "heart", Heart::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Kidney> KIDNEY = registerItem(
            "kidney", Kidney::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Liver> LIVER = registerItem(
            "liver", Liver::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Lungs> LUNGS = registerItem(
            "lungs", Lungs::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Muscle> MUSCLE = registerItem(
            "muscle", Muscle::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Neuro> NEURO = registerItem(
            "neuro", Neuro::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Skin> SKIN = registerItem(
            "skin", Skin::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, SpinalCord> SPINAL_CORD = registerItem(
            "spinal_cord", SpinalCord::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item, Stomach> STOMACH = registerItem(
            "stomach", Stomach::new,
            new Item.Properties().stacksTo(1)
    );

    private static <T extends Item> IEntry<Item, T> registerItem(String name, Function<Item.Properties, ? extends T> func, Item.Properties properties) {
        IEntry<Item, T> item = PlatformService.REGISTRY_HANDLER.registerItem(name, () -> func.apply(properties));
        ITEMS_SET.add(item);
        return item;
    }

    private static IEntry<Item, BlockItem> registerSimpleBlockItem(String name, Supplier<? extends Block> block, Item.Properties properties) {
        IEntry<Item, BlockItem> blockItem = PlatformService.REGISTRY_HANDLER.registerItem(name, () -> new BlockItem(block.get(), properties));
        ITEMS_SET.add(blockItem);
        return blockItem;
    }

    public static void register() {
    }
}
