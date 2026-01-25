package com.lastimp.dgh.source.register;

import com.google.common.collect.ImmutableSet;
import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModVillagers {
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSION = DeferredRegister.create(Registries.VILLAGER_PROFESSION, DontGetHurt.MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, DontGetHurt.MODID);

    public static final DeferredHolder<PoiType, PoiType> DOCTOR_POI = POI_TYPES.register("doctor_poi",
            () -> new PoiType(
                    Set.copyOf(ModBlocks.OPERATING_BED_BLOCK.get().getStateDefinition().getPossibleStates()),
                    1, 1
            )
    );

    public static final DeferredHolder<VillagerProfession, VillagerProfession> DOCTOR_MAKER = VILLAGER_PROFESSION.register("doctor",
            () -> new VillagerProfession(
                    "doctor",
                    holder -> holder.is(DOCTOR_POI.getKey()),
                    holder -> holder.is(DOCTOR_POI.getKey()),
                    ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_BUTCHER
            )
    );

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == DOCTOR_MAKER.get()) {
            List<VillagerTrades.ItemListing> level1Trades = event.getTrades().get(1);
            List<VillagerTrades.ItemListing> level2Trades = event.getTrades().get(2);
            List<VillagerTrades.ItemListing> level3Trades = event.getTrades().get(3);
            List<VillagerTrades.ItemListing> level4Trades = event.getTrades().get(4);
            List<VillagerTrades.ItemListing> level5Trades = event.getTrades().get(5);

            // 1级交易：绷带、血包、石膏
            level1Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.WHITE_WOOL, 18), // 玩家出售的物品
                    new ItemStack(Items.EMERALD, 1), // 玩家获得的物品
                    16, 2, 0.05F
            ));
            level1Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.POPPY, 8), // 玩家出售的物品
                    new ItemStack(Items.EMERALD, 1), // 玩家获得的物品
                    16, 2, 0.05F
            ));
            level1Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1), // 玩家出售的物品
                    new ItemStack(ModItems.BANDAGE.get(), 3), // 玩家获得的物品
                    16, 1, 0.05F
            ));
            level1Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2), // 玩家出售的物品
                    Optional.of(new ItemCost(ModItems.BLOOD_PACK_EMPTY.get(), 1)), // 玩家获得的物品
                    new ItemStack(ModItems.BLOOD_PACK.get(), 1), // 玩家获得的物品
                    16, 1, 0.2F
            ));
            level1Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3), // 玩家出售的物品
                    new ItemStack(ModItems.GYPSUM.get(), 1), // 玩家获得的物品
                    16, 1, 0.2F
            ));

            // 2级交易：消毒剂、吗啡、缝合线
            level2Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.CHARCOAL, 26), // 玩家出售的物品
                    new ItemStack(Items.EMERALD, 1), // 玩家获得的物品
                    16, 10, 0.05F
            ));
            level2Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.SPIDER_EYE, 4), // 玩家出售的物品
                    new ItemStack(Items.EMERALD, 1), // 玩家获得的物品
                    16, 10, 0.05F
            ));
            level2Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3), // 玩家出售的物品
                    new ItemStack(ModItems.ANTISEPTIC.get(), 1), // 玩家获得的物品
                    16, 5, 0.2F
            ));
            level2Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4), // 玩家出售的物品
                    new ItemStack(ModItems.MORPHINE.get(), 1), // 玩家获得的物品
                    16, 5, 0.2F
            ));
            level2Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2), // 玩家出售的物品
                    new ItemStack(ModItems.SUTURE.get(), 6), // 玩家获得的物品
                    16, 5, 0.05F
            ));

            // 3级交易：气胸针、止血带、广谱抗生素、骨骼植入物
            level3Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.NEEDLE.get(), 2),
                    10, 10, 0.05F
            ));
            level3Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ModItems.TOURNIQUET.get(), 1),
                    10, 10, 0.2F
            ));
            level3Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 7),
                    new ItemStack(ModItems.ANTIBIOTICS.get(), 1),
                    10, 10, 0.2F
            ));
            level3Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 6),
                    new ItemStack(ModItems.BONE_IMPLANTS.get(), 1),
                    10, 10, 0.2F
            ));

            // 4级交易：引流管、医用支架、肾上腺素、吗啡酮
            level4Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.NEEDLE.get(), 2),
                    10, 15, 0.05F
            ));
            level4Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ModItems.MEDICAL_STENT.get(), 1),
                    10, 15, 0.2F
            ));
            level4Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(ModItems.ADRENALINE.get(), 1),
                    10, 15, 0.2F
            ));
            level4Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 10),
                    new ItemStack(ModItems.NALOXONE.get(), 1),
                    10, 15, 0.2F
            ));

            // 5级交易：停滞袋、甘露醇
            level5Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 32),
                    new ItemStack(ModItems.STASIS_BAG.get(), 1),
                    4, 30, 0.2F
            ));
            level5Trades.add((trader, rand) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 20),
                    new ItemStack(ModItems.MANNITOL.get(), 1),
                    4, 30, 0.2F
            ));
        }
    }

    public static void register(IEventBus eventBus) {
        VILLAGER_PROFESSION.register(eventBus);
        POI_TYPES.register(eventBus);
    }
}
