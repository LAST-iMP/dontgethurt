package com.lastimp.dgh.source.item.organs;

import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractOrgan;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.tags.ModTags;
import com.lastimp.dgh.source.core.bodyPart.Head;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import static com.lastimp.dgh.DontGetHurt.DELTA;
import static com.lastimp.dgh.api.bodyPart.BodyCondition.BRAIN_DAMAGE;

public class Brain extends AbstractOrgan {
    public Brain(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack update(ItemStack stack, HealthCapability health, AbstractBody body, LivingEntity entity) {
        Head head = (Head) health.getComponent(BodyComponents.HEAD);
        int num = head.countOrganMatch(ModTags.BRAIN);
        float factor = (float) (2 * num / (1 + Math.sqrt(num)));
        head.healing(BRAIN_DAMAGE, -BodyCondition.get(BRAIN_DAMAGE).healingSpeed() * DELTA * factor);
        return stack;
    }
}
