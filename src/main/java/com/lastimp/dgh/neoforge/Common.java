package com.lastimp.dgh.neoforge;

import com.lastimp.dgh.source.core.capability.HealthCapability;
import com.lastimp.dgh.source.core.capability.InjuryRecord;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.LinkedList;
import java.util.List;

public class Common {
    public static ValueInput rebuild(HealthCapability health, HolderLookup.Provider lookup) {
        TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        health.serialize(output);
        return TagValueInput.create(ProblemReporter.DISCARDING, lookup, output.buildResult());
    }

    public static CompoundTag rebuildTag(HealthCapability health) {
        TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        health.serialize(output);
        return output.buildResult();
    }

    public static Identifier getId(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier getIdBySeperator(String path, char seperator) {
        return Identifier.bySeparator(path, seperator);
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
