package com.lastimp.dgh.neoforge;

import com.lastimp.dgh.source.core.capability.InjuryRecord;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.component.WrittenBookContent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.LinkedList;
import java.util.List;

public class Common {
    public static ResourceLocation ResourceLocation(String path) {
        return ResourceLocation.parse(path);
    }

    public static ResourceLocation ResourceLocation(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation ResourceBySeperator(String path, char seperator) {
        return ResourceLocation.bySeparator(path, seperator);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    public static void sendToPlayersNear(ServerLevel level, ServerPlayer excluded, double x, double y, double z, double radius, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayersNear(level, excluded, x, y, z, radius, payload);
    }

    public static WrittenBookContent getBookTag(Component title, Component author, List<InjuryRecord> recordList) {
        var pages = new LinkedList<Filterable<Component>>();
        writeToBook(recordList, pages);
        return new WrittenBookContent(Filterable.passThrough(title.getString()), author.getString(), 0, pages, true);
    }

    private static void writeToBook(List<InjuryRecord> recordList, List<Filterable<Component>> pages) {
        int lineCount = 0;
        StringBuilder builder = new StringBuilder();

        for (var e : recordList) {
            for (var record : e.toString().split("\n")) {
                while (!record.isEmpty()) {
                    if (lineCount >= 14) {
                        pages.add(Filterable.passThrough(Component.literal(builder.toString())));
                        builder.setLength(0);;
                        lineCount = 0;
                    }
                    var maxLength = Math.min(16, record.length());
                    builder.append(record.substring(0, maxLength).trim()).append("\n");
                    record = record.substring(maxLength);
                    lineCount++;
                }
            }
        }
        if (!builder.isEmpty()) {
            pages.add(Filterable.passThrough(Component.literal(builder.toString())));
        }
    }
}
