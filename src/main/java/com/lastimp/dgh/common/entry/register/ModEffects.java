package com.lastimp.dgh.common.entry.register;

import com.lastimp.dgh.common.PlatformService;
import com.lastimp.dgh.common.buffs.SymptomsEffect;
import com.lastimp.dgh.common.buffs.buff.AdrenalineEffect;
import com.lastimp.dgh.common.buffs.buff.CureEffect;
import com.lastimp.dgh.common.buffs.buff.FoodConsumerEffect;
import com.lastimp.dgh.common.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.common.buffs.debuff.IntensePainEffect;
import com.lastimp.dgh.common.buffs.debuff.PulseEffect;
import com.lastimp.dgh.common.buffs.debuff.StaggerEffect;
import com.lastimp.dgh.common.entry.IEntry;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class ModEffects {
    public static final IEntry<MobEffect> STAGGER_EFFECT = registerEffect(
            "stagger_effect", () -> new StaggerEffect(0xFFE74F52)
    );

    public static final IEntry<MobEffect> INTENSE_PAIN_EFFECT = registerEffect(
            "intense_pain_effect", () -> new IntensePainEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> SWEATING_EFFECT = registerEffect(
            "sweating_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> CRAVING_EFFECT = registerEffect(
            "craving_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> PALE_SKIN_EFFECT = registerEffect(
            "pale_skin_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> HARD_BREATH_EFFECT = registerEffect(
            "hard_breath_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> INCREASED_HEARTRATE_EFFECT = registerEffect(
            "increased_heartrate_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> KEEP_LIVING_EFFECT = registerEffect(
            "keep_living_effect", () -> new KeepLivingEffect(0xFF88FFD4)
    );

    public static final IEntry<MobEffect> CURE_EFFECT = registerEffect(
            "cure_effect", () -> new CureEffect(0xFF88FFD4)
    );

    public static final IEntry<MobEffect> INFLAMMATION_EFFECT = registerEffect(
            "inflammation_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> FEVER_EFFECT = registerEffect(
            "fever_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> ADRENALINE_EFFECT = registerEffect(
            "adrenaline_effect", () -> new AdrenalineEffect(0xFF00FF00)
    );

    public static final IEntry<MobEffect> COMBAT_STIMULANT_EFFECT = registerEffect(
            "combat_stimulant_effect", () -> new SymptomsEffect(0xFF00FF00)
    );

    public static final IEntry<MobEffect> ANALGESIA_POISON_EFFECT = registerEffect(
            "analgesia_poison_effect", () -> new PulseEffect(0xFFFFBE4F)
    );

    public static final IEntry<MobEffect> FOOD_CONSUMER_EFFECT = registerEffect(
            "food_consumer_effect", () -> new FoodConsumerEffect(0xFF88FFD4)
    );

    private static IEntry<MobEffect> registerEffect(String name, Supplier<MobEffect> supplier) {
        return PlatformService.REGISTRY_HANDLER.registerEffect(name, supplier);
    }


    public static void register() {
    }
}
