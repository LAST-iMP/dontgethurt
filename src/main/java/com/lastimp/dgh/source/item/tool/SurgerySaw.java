package com.lastimp.dgh.source.item.tool;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.bodyPart.AbstractBody;
import com.lastimp.dgh.api.bodyPart.AbstractVisibleBody;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.healingItems.AbstractPartlyHealItem;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.player.PlayerDyingHandler;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.lastimp.dgh.source.register.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.NotNull;

import static com.lastimp.dgh.api.bodyPart.BodyCondition.*;

public class SurgerySaw extends AbstractPartlyHealItem {
    private static final ResourceLocation BONE_NATURAL = Common.ResourceLocation(DontGetHurt.MODID, "bone_natural");

    public SurgerySaw(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean healOn(@NotNull ServerPlayer source, @NotNull ServerPlayer target, BodyComponents component) {
        return PlayerHealthCapability.getAndSet(target, (h) -> {
            AbstractVisibleBody body = (AbstractVisibleBody) h.getComponent(component);
            if (!body.abnormal(RETRACTED_SKIN)) return false;
            if (body.abnormal(SAWED_BONES)) return false;
            if (component == BodyComponents.HEAD) {
                ItemStack head = new ItemStack(Items.PLAYER_HEAD);
                head.set(DataComponents.PROFILE, new ResolvableProfile(target.getGameProfile()));
                if (!source.addItem(head)) {
                    source.drop(head, true, true);
                }
                PlayerDyingHandler.setDead(target);
            } else {
                body.setConditionValue(SAWED_BONES, BodyCondition.get(SAWED_BONES).maxValue());
                body.setConditionValue(DRILLED_BONES, BodyCondition.get(DRILLED_BONES).defaultValue());
                this.saw(h, source, component);
            }
            return true;
        });
    }

    protected void saw(@NotNull PlayerHealthCapability health, ServerPlayer player, BodyComponents component) {
        int boneNumMax = (component == BodyComponents.TORSO) ? 8 : 2;
        AbstractVisibleBody body = (AbstractVisibleBody) health.getComponent(component);

        int boneReturn = (int) (boneNumMax * (1.0 - Math.min(1.0, body.getConditionValue(FRACTURE))));
        body.setConditionValue(FRACTURE, BodyCondition.get(FRACTURE).minValue());

        ResourceLocation boneKey = body.boneCrafted();
        if (boneKey == null) {
            drop(ModItems.BONE_NATURAL.get(), player, boneReturn);
        } else {
            drop(BodyCondition.bones.get(boneKey).get(), player, boneReturn);
            body.setConditionValue(boneKey, BodyCondition.get(boneKey).defaultValue());
        }
    }

    public static void sawExcept(ServerPlayer source, AbstractBody body, ResourceLocation exception, int maxAmount) {
        int boneReturn = (int) (maxAmount * (1.0 - Math.min(1.0, body.getConditionValue(FRACTURE))));
        for (var key : BodyCondition.bones.keySet()) {
            if (body.abnormal(key) && key != exception) {
                drop(BodyCondition.bones.get(key).get(), source, (int)(boneReturn * body.getConditionValue(key)));
                body.setConditionValue(key, BodyCondition.get(key).defaultValue());
            }
        }
        if (exception != null) {
            drop(ModItems.BONE_NATURAL.get(), source, (int)(boneReturn * (1.0f - body.getConditionValue(SAWED_BONES))));
            body.setConditionValue(SAWED_BONES, BodyCondition.get(SAWED_BONES).maxValue());
        }
    }

    private static void drop(Item item, ServerPlayer player, int amount) {
        var stack=  new ItemStack(item, amount);
        if (!player.addItem(stack)) {
            player.drop(stack, true, true);
        }
    }
}
