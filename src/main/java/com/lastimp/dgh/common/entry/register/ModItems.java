package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.entry.IEntry;
import com.lastimp.dgh.common.item.medicine.*;
import com.lastimp.dgh.common.item.organs.*;
import com.lastimp.dgh.common.item.tool.*;
import com.lastimp.dgh.common.capability.bodyPart.base.BodyCondition;
import com.lastimp.dgh.common.item.limbs.HumanHand;
import com.lastimp.dgh.common.item.limbs.HumanLeg;
import net.minecraft.world.item.*;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.common.entry.register.ModBlocks.OPERATING_BED_BLOCK;

public class ModItems {
    public static final HashSet<IEntry<?>> ITEMS_SET = new HashSet<>();

    public static final IEntry<Item> OPERATING_BED_BLOCK_ITEM = registerItem(
            "operating_bed", () -> new BedItem(
                    OPERATING_BED_BLOCK.get(),
                    new Item.Properties().stacksTo(1)
            ));

    public static final IEntry<Item> HEALTH_SCANNER = registerItem(
            "health_scanner", () -> new HealthScanner(
                    new Item.Properties().stacksTo(1)
            ));

    public static final IEntry<Item> BLOOD_PACK = registerItem(
            "blood_pack", () -> new BloodPacks(
                    new Item.Properties().stacksTo(16)
            ));

    public static final IEntry<Item> BLOOD_PACK_EMPTY = registerItem(
            "blood_pack_empty", () -> new BloodPacksEmpty(
                    new Item.Properties().stacksTo(16)
            ));

    public static final IEntry<Item> BLOOD_SCANNER = registerItem(
            "blood_scanner", () -> new BloodScanner(
                    new Item.Properties().stacksTo(1)
            ));

    public static final IEntry<Item> BANDAGE = registerItem(
            "bandage", () -> new Bandages(
                    new Item.Properties().stacksTo(64)
            ));

    public static final IEntry<Item> MORPHINE = registerItem(
            "morphine", () -> new Morphine(
                    new Item.Properties().stacksTo(16)
            ));

    public static final IEntry<Item> GYPSUM = registerItem(
            "gypsum", () -> new Gypsum(
                    new Item.Properties().stacksTo(16)
            ));

    public static final IEntry<Item> SUTURE = registerItem(
            "suture", () -> new Sutures(
                    new Item.Properties().stacksTo(64)
            ));

    public static final IEntry<Item> HEALTH_CARE_BAG = registerItem(
            "health_care_bag", () -> new HealthCareBag(
                    new Item.Properties().stacksTo(1)
            ));

    public static final IEntry<Item> SURGERY_TOOL_BAG = registerItem(
            "surgery_tool_bag", SurgeryToolBag::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> LIMB_REF_BEG = registerItem(
            "limb_ref_beg", LimbRefBeg::new,
            new Item.Properties().stacksTo(1));

    public static final IEntry<Item> WOOD_WRENCH = registerItem(
            "wood_wrench", () -> new WoodWrench(
                    new Item.Properties().stacksTo(1)
                            .durability(60)
            ));

    public static final IEntry<Item> SCALPEL = registerItem(
            "scalpel", Scalpel::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> HEMOSTAT = registerItem(
            "hemostat", Hemostat::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> RETRACTOR = registerItem(
            "retractor", Retractor::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> SURGICAL_DRILL = registerItem(
            "surgical_drill", SurgicalDrill::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> TWEEZER = registerItem(
            "tweezer", Tweezer::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> BONE_IMPLANTS = registerItem(
            "bone_implants", BoneImplants::new,
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_WOOD = registerItem(
            "bone_implants_wood", (properties) -> new BoneImplants(properties, BodyCondition.BONE_WOOD),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_STONE = registerItem(
            "bone_implants_stone", (properties) -> new BoneImplants(properties, BodyCondition.BONE_STONE),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_COPPER = registerItem(
            "bone_implants_copper", (properties) -> new BoneImplants(properties, BodyCondition.BONE_COPPER),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_IRON = registerItem(
            "bone_implants_iron", (properties) -> new BoneImplants(properties, BodyCondition.BONE_IRON),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_GOLD = registerItem(
            "bone_implants_gold", (properties) -> new BoneImplants(properties, BodyCondition.BONE_GOLD),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_DIMOND = registerItem(
            "bone_implants_dimond", (properties) -> new BoneImplants(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> BONE_IMPLANTS_NETHERITE = registerItem(
            "bone_implants_netherite", (properties) -> new BoneImplants(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties().stacksTo(1).durability(8)
    );

    public static final IEntry<Item> SURGERY_SAW = registerItem(
            "surgery_saw", SurgerySaw::new,
            new Item.Properties().stacksTo(1).durability(300)
    );

    public static final IEntry<Item> BONE_NATURAL = registerItem(
            "bone_natural", (properties) -> new SurgeryBones(properties, null),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_WOOD = registerItem(
            "bone_wood", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_WOOD),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_STONE = registerItem(
            "bone_stone", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_STONE),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_COPPER = registerItem(
            "bone_copper", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_COPPER),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_IRON = registerItem(
            "bone_iron", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_IRON),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_GOLD = registerItem(
            "bone_gold", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_GOLD),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_DIMOND = registerItem(
            "bone_dimond", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BONE_NETHERITE = registerItem(
            "bone_netherite", (properties) -> new SurgeryBones(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> NALOXONE = registerItem(
            "naloxone", Naloxone::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> MEDICAL_STENT = registerItem(
            "medical_stent", MedicalStent::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> TOURNIQUET = registerItem(
            "tourniquet", Tourniquet::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> NEEDLE = registerItem(
            "needle", Needle::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> DRAINAGE = registerItem(
            "drainage", Drainage::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> ADRENALINE = registerItem(
            "adrenaline", Adrenaline::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> OXYGEN_MASK = registerItem(
            "oxygen_mask", OxygenMask::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item> ANTIBIOTIC_OINTMENT = registerItem(
            "antibiotic_ointment", AntibioticOintment::new,
            new Item.Properties().stacksTo(1).durability(4).setNoRepair()
    );

    public static final IEntry<Item> ANTISEPTIC_SPRAYER = registerItem(
            "antiseptic_sprayer", AntisepticSprayer::new,
            new Item.Properties().stacksTo(1).durability(10)
    );

    public static final IEntry<Item> ANTISEPTIC = registerItem(
            "antiseptic", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item> AUTOPULSE = registerItem(
            "autopulse", Autopulse::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item> ANTIBIOTICS = registerItem(
            "antibiotics", Antibiotics::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> HUMAN_HAND = registerItem(
            "human_hand", HumanHand::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> HUMAN_LEG = registerItem(
            "human_leg", HumanLeg::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> PLASTIC_SKIN = registerItem(
            "plastic_skin", PlasticSkin::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item> ANTIBIOTIC_GLUE = registerItem(
            "antibiotic_glue", AntibioticGlue::new,
            new Item.Properties().stacksTo(1).durability(4)
    );

    public static final IEntry<Item> STASIS_BAG = registerItem(
            "stasis_bag", StasisBag::new,
            new Item.Properties().stacksTo(1).durability(400)
    );

    public static final IEntry<Item> WALKING_STICK = registerItem(
            "walking_stick", Item::new,
            new Item.Properties().stacksTo(2)
    );

    public static final IEntry<Item> STRETCHER = registerItem(
            "stretcher", StretcherItem::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> MANNITOL = registerItem(
            "mannitol", Mannitol::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> AUTO_USE_BAG = registerItem(
            "auto_use_bag", AutoUseBag::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> MEDICINE_BAG = registerItem(
            "medicine_bag", MedicineBag::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> HERB_BANDAGE = registerItem(
            "herb_bandage", HerbBandage::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item> CLAMP = registerItem(
            "clamp", Clamp::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> GRASS_STRING = registerItem(
            "grass_string", Item::new,
            new Item.Properties().stacksTo(64)
    );

    public static final IEntry<Item> AED = registerItem(
            "aed", com.lastimp.dgh.common.item.tool.AED::new,
            new Item.Properties().stacksTo(1).durability(20).setNoRepair()
    );

    public static final IEntry<Item> FOOD_CONSUMER = registerItem(
            "food_consumer", FoodConsumer::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> FENTANYL = registerItem(
            "fentanyl", Fentanyl::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> HYPERZINE = registerItem(
            "hyperzine", Hyperzine::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> HARDENER = registerItem(
            "hardener", Hardener::new,
            new Item.Properties().stacksTo(16)
    );

    public static final IEntry<Item> BRAIN = registerItem(
            "brain", Brain::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> EYE = registerItem(
            "eye", Eye::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> HEART = registerItem(
            "heart", Heart::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> KIDNEY = registerItem(
            "kidney", Kidney::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> LIVER = registerItem(
            "liver", Liver::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> LUNGS = registerItem(
            "lungs", Lungs::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> MUSCLE = registerItem(
            "muscle", Muscle::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> NEURO = registerItem(
            "neuro", Neuro::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> SKIN = registerItem(
            "skin", Skin::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> SPINAL_CORD = registerItem(
            "spinal_cord", SpinalCord::new,
            new Item.Properties().stacksTo(1)
    );

    public static final IEntry<Item> STOMACH = registerItem(
            "stomach", Stomach::new,
            new Item.Properties().stacksTo(1)
    );

    public static void register() {
    }

    private static IEntry<Item> registerItem(final String name, final Supplier<Item> sup) {
        IEntry<Item> item = PlatformService.REGISTRY_HANDLER.registerItem(name, sup);
        ITEMS_SET.add(item);
        return item;
    }

    private static IEntry<Item> registerItem(String name, Function<Item.Properties, ? extends Item> func, Item.Properties properties) {
        IEntry<Item> item = PlatformService.REGISTRY_HANDLER.registerItem(name, () -> func.apply(properties));
        ITEMS_SET.add(item);
        return item;
    }
}
