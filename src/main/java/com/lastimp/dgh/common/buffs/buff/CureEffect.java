package com.lastimp.dgh.common.buffs.buff;

import com.lastimp.dgh.common.capability.HealthCapability;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CureEffect extends MobEffect {
    public static String ID_DAMAGE = "efaa5782-b172-4048-b0cb-48622290b4a4";

    public CureEffect(int color) {
        super(MobEffectCategory.BENEFICIAL, color);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity livingEntity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(livingEntity, attributeMap, amplifier);

        AttributeInstance damage = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);

        double amount = 2f * (amplifier + 1);
        AttributeModifier move_modifier = new AttributeModifier(
                UUID.fromString(ID_DAMAGE),
                "cure_damage_effect",
                amount,
                AttributeModifier.Operation.ADDITION
        );

        if (damage != null) {
            damage.removeModifier(UUID.fromString(ID_DAMAGE));
            damage.addTransientModifier(move_modifier);
        }
        this.onVillagerAddEffect(livingEntity, amplifier);
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);

        AttributeInstance damage = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
        if (damage != null)
            damage.removeModifier(UUID.fromString(ID_DAMAGE));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getAbsorptionAmount() < amplifier * 2 + 2)
            livingEntity.setAbsorptionAmount(amplifier * 2 + 2);
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
