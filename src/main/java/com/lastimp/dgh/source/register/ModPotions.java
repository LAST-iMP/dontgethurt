package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber(modid = DontGetHurt.MODID)
public class ModPotions {
    public static final DeferredRegister<Potion> MOD_POTIONS = DeferredRegister.create(Registries.POTION, DontGetHurt.MODID);
    public static final Map<DeferredHolder<Potion, Potion>, String> POTIONS = new HashMap<>();

    public static void register(IEventBus eventBus) {
        MOD_POTIONS.register(eventBus);
    }

    public static final DeferredHolder<Potion, Potion> COMBAT_STIMULANT_POTION = register(
            "combat_stimulant", () -> new Potion("combat_stimulant", new MobEffectInstance(ModEffects.COMBAT_STIMULANT_EFFECT, 20 * 75))
    );

    public static final DeferredHolder<Potion, Potion> ANALGESIA_POISON_POTION = register(
            "analgesia_poison", () -> new Potion("analgesia_poison", new MobEffectInstance(ModEffects.ANALGESIA_POISON_EFFECT, 20 * 60))
    );

    private static DeferredHolder<Potion, Potion> register(String name, Supplier<Potion> sup) {
        var holder = MOD_POTIONS.register(name, sup);
        POTIONS.put(holder, name);
        return holder;
    }

    @SubscribeEvent // on the game event bus
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.STRONG_REGENERATION, ModItems.MORPHINE.asItem(), ModPotions.COMBAT_STIMULANT_POTION);
        builder.addMix(Potions.STRONG_HARMING, ModItems.MORPHINE.asItem(), ModPotions.ANALGESIA_POISON_POTION);
    }

}
