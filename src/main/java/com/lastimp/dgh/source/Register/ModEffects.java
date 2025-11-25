package com.lastimp.dgh.source.Register;

import com.lastimp.dgh.DontGetHurt;
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
            "stagger_effect", () -> new StaggerEffect(0xFFFF7300)
    );

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
