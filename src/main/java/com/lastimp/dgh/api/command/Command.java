package com.lastimp.dgh.api.command;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.bodyPart.BodyCondition;
import com.lastimp.dgh.neoforge.Common;
import com.lastimp.dgh.source.core.menu.menuProvider.HealthMenuProvider;
import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DontGetHurt.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Command {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(DontGetHurt.MODID)
                        .then(Commands.literal("component")
                                .then(Commands.literal("list")
                                        .executes(Command::listAllComponent)
                                )
                        )
                        .then(Commands.literal("condition")
                                .then(Commands.literal("list")
                                        .executes(Command::listAllCondition)
                                )
                                .then(Commands.literal("listOf")
                                        .then(Commands.argument("Component", StringArgumentType.string())
                                                .executes(Command::listConditionOf)
                                        )
                                )
                                .then(Commands.literal("add")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("entity", EntityArgument.entities())
                                                .then(Commands.argument("Component", StringArgumentType.string())
                                                        .then(Commands.argument("Condition", StringArgumentType.string())
                                                                .then(Commands.argument("value", FloatArgumentType.floatArg(-2, 2))
                                                                        .executes(Command::addComponentCondition))
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("reset")
                                        .requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("entity", EntityArgument.entities())
                                                .then(Commands.argument("Component", StringArgumentType.string())
                                                        .then(Commands.argument("Condition", StringArgumentType.string())
                                                                .executes(Command::resetComponentCondition)
                                                        )
                                                        .executes(Command::resetComponent)
                                                )
                                                .executes(Command::reset)
                                        )
                                )
                        )
                        .then(Commands.literal("health_menu")
                                .requires(source -> source.hasPermission(2))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(Command::openFullMenu)
                                )
                        )
        );
    }

    public static int openFullMenu(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer source = context.getSource().getPlayer();
        ServerPlayer target = EntityArgument.getPlayer(context, "player");
        if (source != null)
            HealthMenuProvider.open(source, target.getUUID(), true);
        return 1;
    }

    public static int listAllComponent(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> {
                    var message = Component.empty();
                    for (var condition : BodyComponents.values())
                        message.append(condition.name().toLowerCase() + ", ");
                    return message;
                },true
        );
        return 1;
    }

    public static int listAllCondition(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> {
                    var message = Component.empty();
                    for (var condition : BodyCondition.conditions.keySet())
                        message.append(condition + ", ");
                    return message;
                },true
        );
        return 1;
    }

    public static int listConditionOf(CommandContext<CommandSourceStack> context) {
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());

        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> {
                    var message = Component.empty();
                    for (var condition : new HealthCapability().getComponent(components).getBodyConditions())
                        message.append(condition + ", ");
                    return message;
                },true
        );
        return 1;
    }

    public static int addComponentCondition(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var entities = EntityArgument.getEntities(context, "entity");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());
        var condition = Common.ResourceBySeperator(StringArgumentType.getString(context, "Condition"), ':');
        float value = FloatArgumentType.getFloat(context, "value");

        CommandSourceStack source = context.getSource();
        for (var entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (!HealthCapability.has(livingEntity)) continue;
            source.sendSuccess(
                    () -> Component.literal("已将实体" + livingEntity.getScoreboardName() + "的" + components + BodyCondition.get(condition) + "增加" + value),
                    true
            );
            HealthCapability.getAndSet(livingEntity, h -> {
                h.getComponent(components).addConditionValue(condition, value);
                return 1;
            });
        }

        return 1;
    }

    public static int resetComponentCondition(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var entities = EntityArgument.getEntities(context, "entity");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());
        var condition = Common.ResourceBySeperator(StringArgumentType.getString(context, "Condition"), ':');

        CommandSourceStack source = context.getSource();
        for (var entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (!HealthCapability.has(livingEntity)) continue;
            source.sendSuccess(
                    () -> Component.literal("已将实体" + livingEntity.getScoreboardName() + "的" + components + BodyCondition.get(condition) + "重置"),
                    true
            );
            HealthCapability.getAndSet(livingEntity, h -> {
                h.getComponent(components).setConditionValue(condition, BodyCondition.get(condition).defaultValue());
                return 1;
            });
        }
        return 1;
    }

    public static int resetComponent(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var entities = EntityArgument.getEntities(context, "entity");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());

        CommandSourceStack source = context.getSource();
        for (var entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (!HealthCapability.has(livingEntity)) continue;
            source.sendSuccess(
                    () -> Component.literal("已将实体" + livingEntity.getScoreboardName() + "的" + components + "重置"),
                    true
            );
            HealthCapability.getAndSet(livingEntity, h -> {
                var body = h.getComponent(components);
                for (var condition : body.getBodyConditions()) {
                    body.setConditionValue(condition, BodyCondition.get(condition).defaultValue());
                }
                return 1;
            });
        }
        return 1;
    }

    public static int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        var entities = EntityArgument.getEntities(context, "entity");

        CommandSourceStack source = context.getSource();
        for (var entity : entities) {
            if (!(entity instanceof LivingEntity livingEntity)) continue;
            if (!HealthCapability.has(livingEntity)) continue;
            source.sendSuccess(() -> Component.literal("已将实体" + livingEntity.getScoreboardName() + "重置"), true);

            var oldHealth = HealthCapability.get(livingEntity);
            var newHealth = new HealthCapability();
            newHealth.autoPulse().setStackInSlot(0, oldHealth.autoPulse().getStackInSlot(0));
            newHealth.oxygenMask().setStackInSlot(0, oldHealth.oxygenMask().getStackInSlot(0));
            oldHealth.deserializeNBT(newHealth.serializeNBT());
        }
        return 1;
    }
}
