package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractOrgan;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.bodyPart.Torso;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.enums.BodyComponents.*;

public class Heart extends AbstractOrgan {
    public Heart(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Torso torso = (Torso) health.getComponent(TORSO);
        if (torso.heartStable()) {
            int num = torso.countOrganMatch(ModTags.HEART);
            float factor = (float) (2 * num / (1 + Math.sqrt(num)));
            torso.addHeartRate(-DELTA / 30.0f * factor);
        }
        return stack;
    }
}
