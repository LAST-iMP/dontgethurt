package com.lastimp.dgh.api.command;

import com.lastimp.dgh.DontGetHurt;
import com.lastimp.dgh.api.enums.BodyComponents;
import com.lastimp.dgh.api.enums.BodyCondition;
import com.lastimp.dgh.source.core.player.PlayerHealthCapability;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = DontGetHurt.MODID, bus = EventBusSubscriber.Bus.GAME)
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
                                        .then(Commands.argument("player", EntityArgument.player())
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
                                        .then(Commands.argument("player", EntityArgument.player())
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
        );
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
                    for (var condition : BodyCondition.values())
                        message.append(condition.name().toLowerCase() + ", ");
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
                    for (var condition : new PlayerHealthCapability().getComponent(components).getBodyConditions())
                        message.append(condition.name().toLowerCase() + ", ");
                    return message;
                },true
        );
        return 1;
    }

    public static int addComponentCondition(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());
        BodyCondition condition = BodyCondition.valueOf(StringArgumentType.getString(context, "Condition").toUpperCase());
        float value = FloatArgumentType.getFloat(context, "value");

        CommandSourceStack source = context.getSource();
        source.sendSuccess(
                () -> Component.literal("已将玩家" + player.getScoreboardName() + "的" + components + condition + "增加" + value),
                true
        );

        return PlayerHealthCapability.getAndSet(player, h -> {
            h.getComponent(components).addConditionValue(condition, value);
            return 1;
        });
    }

    public static int resetComponentCondition(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());
        BodyCondition condition = BodyCondition.valueOf(StringArgumentType.getString(context, "Condition").toUpperCase());

        CommandSourceStack source = context.getSource();
        source.sendSuccess(
                () -> Component.literal("已将玩家" + player.getScoreboardName() + "的" + components + condition + "重置"),
                true
        );

        return PlayerHealthCapability.getAndSet(player, h -> {
            h.getComponent(components).setConditionValue(condition, condition.defaultValue);
            return 1;
        });
    }

    public static int resetComponent(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        BodyComponents components = BodyComponents.valueOf(StringArgumentType.getString(context, "Component").toUpperCase());

        CommandSourceStack source = context.getSource();
        source.sendSuccess(
                () -> Component.literal("已将玩家" + player.getScoreboardName() + "的" + components + "重置"),
                true
        );

        return PlayerHealthCapability.getAndSet(player, h -> {
            var body = h.getComponent(components);
            for (var condition : body.getBodyConditions()) {
                body.setConditionValue(condition, condition.defaultValue);
            }
            return 1;
        });
    }

    public static int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");

        CommandSourceStack source = context.getSource();
        source.sendSuccess(() -> Component.literal("已将玩家" + player.getScoreboardName() + "重置"), true);

        PlayerHealthCapability.set(player, new PlayerHealthCapability());
        return 1;
    }
}
