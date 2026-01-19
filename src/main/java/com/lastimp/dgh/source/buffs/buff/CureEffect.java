package com.lastimp.dgh.source.buffs.buff;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.villager.Villager;
import org.jetbrains.annotations.NotNull;

public class CureEffect extends MobEffect {
    public CureEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
        this.setBlendDuration(0);

        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                Identifier.fromNamespaceAndPath(DontGetHurt.MODID, "cure_damage"),
                2f,
                AttributeModifier.Operation.ADD_VALUE
        );
        this.addAttributeModifier(
                Attributes.MAX_ABSORPTION,
                Identifier.fromNamespaceAndPath(DontGetHurt.MODID, "cure_absorb"),
                2f,
                AttributeModifier.Operation.ADD_VALUE
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getAbsorptionAmount() < amplifier * 2 + 2)
            livingEntity.setAbsorptionAmount(amplifier * 2 + 2);
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        super.onEffectAdded(livingEntity, amplifier);
        this.onVillagerAddEffect(livingEntity, amplifier);
    }

    private void onVillagerAddEffect(@NotNull LivingEntity entity, int amplifier) {
        if (!(entity instanceof Villager villager)) return;
        HealthCapability.getAndApply(villager, healthCapability -> {
            var lastHealer = healthCapability.lastHealer();
            villager.getGossips().add(lastHealer, GossipType.MINOR_POSITIVE, 25);
            if (amplifier >= 3) {
                villager.getGossips().add(lastHealer, GossipType.MAJOR_POSITIVE, 20);
            }
        });
    }
}
