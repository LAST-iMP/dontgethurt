package com.lastimp.dgh.source.Register;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.buffs.SymptomsEffect;
import com.lastimp.dgh.source.buffs.buff.CureEffect;
import com.lastimp.dgh.source.buffs.buff.KeepLivingEffect;
import com.lastimp.dgh.source.buffs.debuff.StaggerEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DontGetHurt.MODID);

    public static final DeferredHolder<MobEffect, StaggerEffect> STAGGER_EFFECT = MOB_EFFECTS.register(
            "stagger_effect", () -> new StaggerEffect(0xFFE74F52)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> INTENSE_PAIN_EFFECT = MOB_EFFECTS.register(
            "intense_pain_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> SWEATING_EFFECT = MOB_EFFECTS.register(
            "sweating_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, SymptomsEffect> CRAVING_EFFECT = MOB_EFFECTS.register(
            "craving_effect", () -> new SymptomsEffect(0xFFFFBE4F)
    );

    public static final DeferredHolder<MobEffect, KeepLivingEffect> KEEP_LIVING_EFFECT = MOB_EFFECTS.register(
            "keep_living_effect", () -> new KeepLivingEffect(0xFF88FFD4)
    );

    public static final DeferredHolder<MobEffect, CureEffect> CURE_EFFECT = MOB_EFFECTS.register(
            "cure_effect", () -> new CureEffect(0xFF88FFD4)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
