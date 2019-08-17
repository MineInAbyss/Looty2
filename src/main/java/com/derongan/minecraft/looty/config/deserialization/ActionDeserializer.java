package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.component.ComponentRegister;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.logging.Logger;

public class ActionDeserializer implements JsonDeserializer<Action> {
    private final ComponentRegister componentRegister;
    private final Logger logger;

    @Inject
    public ActionDeserializer(ComponentRegister componentRegister, Logger logger) {
        this.componentRegister = componentRegister;
        this.logger = logger;
    }

    @Override
    public Action deserialize(JsonElement json,
                              Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {

        Action.Builder actionBuilder = Action.newBuilder();


        json.getAsJsonObject().entrySet().forEach(entry -> {
            Class<? extends Message> clazz = componentRegister.getMessageForString(entry.getKey().toLowerCase());

            try {
                Method builderMethod = clazz.getMethod("newBuilder");
                Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);

                JsonFormat.parser().merge(entry.getValue().toString(), messageBuilder);

                actionBuilder.addComponent(Any.pack(messageBuilder.build()));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InvalidProtocolBufferException e) {
                logger.warning(String.format("Failed to generate component [%s]", entry.getKey()));
            }
        });

        return actionBuilder.build();
    }
}
