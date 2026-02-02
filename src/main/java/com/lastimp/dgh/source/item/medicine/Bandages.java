
package com.lastimp.dgh.source.item.medicine;

import com.lastimp.dgh.api.bodyPart.ConditionAccessor;
import com.lastimp.dgh.source.core.bodyPart.base.AbstractBody;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.lastimp.dgh.source.core.bodyPart.base.BodyCondition.*;

public class Bandages extends AbstractPartlyHealItem {

    public Bandages(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull LivingEntity source, @NotNull LivingEntity entity, BodyComponents component) {
        return HealthCapability.getAndApply(entity, health -> {
            AbstractBody body = health.getComponent(component);
            float currCondition = body.getConditionValue(BANDAGED);
            if (body.abnormal(SURGERY_INCISION)) return false;
            if (currCondition > 0.75f) return false;

            body.healing(BANDAGED, 0.5f);
            body.setConditionValue(BANDAGED_DIRTY, ConditionAccessor.get(BANDAGED_DIRTY).defaultValue());

            this.coverCondition(body, BURN);
            this.coverCondition(body, OPEN_WOUND);
            this.coverCondition(body, PASS_THROUGH);
            this.coverCondition(body, FRACTURE);
            return true;
        }, false);
    }

    protected void coverCondition(AbstractBody body, ResourceLocation condition) {
        body.injuryHidden(condition, body.getConditionValue(condition));
        body.setConditionValue(condition, ConditionAccessor.get(condition).defaultValue());
    }

    public static boolean cut(LivingEntity target, BodyComponents component) {
        return HealthCapability.getAndApply(target, health -> {
            AbstractBody body = health.getComponent(component);
            if (body.abnormal(BANDAGED)) {
                body.setConditionValue(BANDAGED, ConditionAccessor.get(BANDAGED).defaultValue());
            } else if (body.abnormal(BANDAGED_DIRTY)) {
                body.setConditionValue(BANDAGED_DIRTY, ConditionAccessor.get(BANDAGED_DIRTY).defaultValue());
            } else {
                return false;
            }
            return true;
        }, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("治疗"));
        tooltipComponents.add(Component.literal("·外伤").withStyle(ChatFormatting.BLUE));
    }
}
