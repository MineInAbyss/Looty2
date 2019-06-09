package com.derongan.minecraft.looty.config.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.inject.Inject;
import java.lang.reflect.Type;

public class ProtoJsonSerializer implements JsonSerializer<Message> {
    @Inject
    public ProtoJsonSerializer() {
    }

    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            return new JsonParser().parse(JsonFormat.printer().print(src));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
