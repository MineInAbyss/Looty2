package com.derongan.minecraft.ui.inputs;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class MessageFormater {
    public static List<String> format(Message message) {
        return format(message, 0);
    }

    public static List<String> format(Message message, int indent) {
        return message.getDescriptorForType()
                .getFields()
                .stream()
                .flatMap(field -> {
                    String prefix = ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + field.getName() + ChatColor.RESET + "" + ChatColor.WHITE + " : ";

                    if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) {
                        Message indented = (Message) message.getField(field);

                        return Stream.concat(Stream.of(prefix), format(indented, indent + 1).stream());
                    } else {
                        return Stream.of(prefix + ChatColor.GREEN +
                                message.getField(field));
                    }
                })
                .map(str -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < indent; i++) {
                        stringBuilder.append("  ");
                    }
                    return stringBuilder.append(str).toString();
                })
                .collect(toImmutableList());
    }
}
