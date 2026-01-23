package com.lastimp.dgh.source.register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.buffs.SymptomsEffect;
import com.lastimp.dgh.source.buffs.buff.AdrenalineEffect;
import com.lastimp.dgh.source.buffs.buff.CureEffect;
import com.lastimp.dgh.source.buffs.buff.FoodConsumerEffect;
import com.lastimp.dgh.source.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.source.buffs.debuff.IntensePainEffect;
import com.lastimp.dgh.source.buffs.debuff.PulseEffect;
import com.lastimp.dgh.source.buffs.debuff.StaggerEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DontGetHurt.MODID);

    public static final DeferredHolder<MobEffect, StaggerEffect> STAGGER_EFFECT = MOB_EFFECTS.register(
            "stagger_effect", () -> new StaggerEffect(0xFFE74F52)
    );

    public static final DeferredHolder<MobEffect, IntensePainEffect> INTENSE_PAIN_EFFECT = MOB_EFFECTS.register(
            "intense_pain_effect", () -> new IntensePainEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> SWEATING_EFFECT = MOB_EFFECTS.register(
            "sweating_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> CRAVING_EFFECT = MOB_EFFECTS.register(
            "craving_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> PALE_SKIN_EFFECT = MOB_EFFECTS.register(
            "pale_skin_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> HARD_BREATH_EFFECT = MOB_EFFECTS.register(
            "hard_breath_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> INCREASED_HEARTRATE_EFFECT = MOB_EFFECTS.register(
            "increased_heartrate_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, KeepLivingEffect> KEEP_LIVING_EFFECT = MOB_EFFECTS.register(
            "keep_living_effect", () -> new KeepLivingEffect(0xFF88FFD4)
    );

    public static final DeferredHolder<MobEffect, CureEffect> CURE_EFFECT = MOB_EFFECTS.register(
            "cure_effect", () -> new CureEffect(0xFF88FFD4)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> INFLAMMATION_EFFECT = MOB_EFFECTS.register(
            "inflammation_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> FEVER_EFFECT = MOB_EFFECTS.register(
            "fever_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, AdrenalineEffect> ADRENALINE_EFFECT = MOB_EFFECTS.register(
            "adrenaline_effect", () -> new AdrenalineEffect(0xFF00FF00)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> COMBAT_STIMULANT_EFFECT = MOB_EFFECTS.register(
            "combat_stimulant_effect", () -> new SymptomsEffect(0xFF00FF00)
    );

    public static final DeferredHolder<MobEffect, PulseEffect> ANALGESIA_POISON_EFFECT = MOB_EFFECTS.register(
            "analgesia_poison_effect", () -> new PulseEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, FoodConsumerEffect> FOOD_CONSUMER_EFFECT = MOB_EFFECTS.register(
            "food_consumer_effect", () -> new FoodConsumerEffect(0xFF88FFD4)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
