package com.lastimp.dgh.neoforge;

import com.lastimp.dgh.source.core.capability.InjuryRecord;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    public static CompoundTag getBookTag(Component title, Component author, List<InjuryRecord> recordList) {
        CompoundTag tag = new CompoundTag();
        tag.put("title", StringTag.valueOf(title.getString()));
        tag.put("filtered_title", StringTag.valueOf(title.getString()));
        tag.put("author", StringTag.valueOf(author.getString()));
        tag.put("resolved", ByteTag.valueOf((byte) 1));
        tag.put("pages", new ListTag());
        writeToBook(recordList, tag);
        return tag;
    }

    private static void writeToBook(List<InjuryRecord> recordList, CompoundTag tag) {
        List<String> textList = new LinkedList<>();
        int lineCount = 0;
        StringBuilder builder = new StringBuilder();

        for (var e : recordList) {
            for (var record : e.toString().split("\n")) {
                while (!record.isEmpty()) {
                    if (lineCount >= 14) {
                        textList.add(Component.Serializer.toJson(Component.literal(builder.toString())));
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
            textList.add(Component.Serializer.toJson(Component.literal(builder.toString())));
        }

        ListTag pages = tag.getList("pages", ListTag.TAG_STRING);
        textList.stream().map(StringTag::valueOf).forEach(pages::add);
        tag.put("pages", pages);
    }
}
