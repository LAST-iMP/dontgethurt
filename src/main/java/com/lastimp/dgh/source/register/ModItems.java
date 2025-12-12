package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.source.item.OperatingBedItem;
import com.lastimp.dgh.source.item.tool.*;
import com.lastimp.dgh.source.item.medicine.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.lastimp.dgh.DontGetHurt.MODID;
import static com.lastimp.dgh.source.register.ModBlocks.OPERATING_BED_BLOCK;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DontGetHurt.MODID);

    public static final HashSet<RegistryObject<?>> ITEMS_SET = new HashSet<>();

    public static final RegistryObject<Item> OPERATING_BED_BLOCK_ITEM = registerItem(
            "operating_bed",
            () -> new OperatingBedItem(
                    OPERATING_BED_BLOCK.get(),
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

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

    public static final RegistryObject<Item> BLOOD_SCANNER = registerItem(
            "blood_scanner",
            () -> new BloodScanner(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> BANDAGE = registerItem(
            "bandage",
            () -> new Bandages(
                    new Item.Properties()
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<Item> MORPHINE = registerItem(
            "morphine",
            () -> new Morphine(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Item> GYPSUM = registerItem(
            "gypsum",
            () -> new Gypsum(
                    new Item.Properties()
                            .stacksTo(16)
            )
    );

    public static final RegistryObject<Item> SUTURE = registerItem(
            "suture",
            () -> new Sutures(
                    new Item.Properties()
                            .stacksTo(64)
            )
    );

    public static final RegistryObject<Item> HEALTH_CARE_BAG = registerItem(
            "health_care_bag",
            () -> new HealthCareBag(
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final RegistryObject<Item> SURGERY_TOOL_BAG = registerItem(
            "surgery_tool_bag",
            SurgeryToolBag::new,
            new Item.Properties()
                    .stacksTo(1)
    );

    public static final RegistryObject<Item> WOOD_WRENCH = registerItem(
            "wood_wrench",
            () -> new WoodWrench(
                    new Item.Properties()
                            .stacksTo(1)
                            .durability(60)
            )
    );

    public static final RegistryObject<Item> SCALPEL = registerItem(
            "scalpel",
            Scalpel::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final RegistryObject<Item> HEMOSTAT = registerItem(
            "hemostat",
            Hemostat::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)

    );

    public static final RegistryObject<Item> RETRACTOR = registerItem(
            "retractor",
            Retractor::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final RegistryObject<Item> SURGICAL_DRILL = registerItem(
            "surgical_drill",
            SurgicalDrill::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final RegistryObject<Item> TWEEZER = registerItem(
            "tweezer",
            Tweezer::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS = registerItem(
            "bone_implants",
            BoneImplants::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_WOOD = registerItem(
            "bone_implants_wood",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_WOOD),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_STONE = registerItem(
            "bone_implants_stone",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_STONE),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_COPPER = registerItem(
            "bone_implants_copper",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_COPPER),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_IRON = registerItem(
            "bone_implants_iron",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_IRON),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_GOLD = registerItem(
            "bone_implants_gold",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_GOLD),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_DIMOND = registerItem(
            "bone_implants_dimond",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> BONE_IMPLANTS_NETHERITE = registerItem(
            "bone_implants_netherite",
            (properties) -> new BoneImplants(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
    );

    public static final RegistryObject<Item> SURGERY_SAW = registerItem(
            "surgery_saw",
            SurgerySaw::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(300)
    );

    public static final RegistryObject<Item> BONE_NATURAL = registerItem(
            "bone_natural",
            (properties) -> new SurgeryBones(properties, null),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_WOOD = registerItem(
            "bone_wood",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_WOOD),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_STONE = registerItem(
            "bone_stone",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_STONE),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_COPPER = registerItem(
            "bone_copper",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_COPPER),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_IRON = registerItem(
            "bone_iron",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_IRON),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_GOLD = registerItem(
            "bone_gold",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_GOLD),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_DIMOND = registerItem(
            "bone_dimond",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_DIMOND),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static final RegistryObject<Item> BONE_NETHERITE = registerItem(
            "bone_netherite",
            (properties) -> new SurgeryBones(properties, BodyCondition.BONE_NETHERITE),
            new Item.Properties()
                    .stacksTo(16)
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static <T extends Item> RegistryObject<T> registerItem(final String name, final Supplier<T> sup) {
        RegistryObject<T> item = ITEMS.register(name, sup);
        ITEMS_SET.add(item);
        return item;
    }

    private static <T extends Item> RegistryObject<T> registerItem(final String name, Function<Item.Properties, ? extends T> func, Item.Properties properties) {
        RegistryObject<T> item = ITEMS.register(name, () -> func.apply(properties));
        ITEMS_SET.add(item);
        return item;
    }
}
