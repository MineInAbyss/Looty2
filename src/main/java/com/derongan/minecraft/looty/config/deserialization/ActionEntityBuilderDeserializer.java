package com.derongan.minecraft.looty.config.deserialization;

import com.derongan.minecraft.looty.skill.ActionEntityBuilder;
import com.derongan.minecraft.looty.skill.component.Component;
import com.google.gson.*;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ActionEntityBuilderDeserializer implements JsonDeserializer<ActionEntityBuilder> {
    @Inject
    public ActionEntityBuilderDeserializer() {
    }

    @Override
    public ActionEntityBuilder deserialize(JsonElement json,
                                           Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {

        ActionEntityBuilder actionEntityBuilder = new ActionEntityBuilder();
        json.getAsJsonObject().entrySet().forEach(entry -> {
            String componentName = StringUtils.capitalize(entry.getKey());
            JsonObject protoInfo = entry.getValue().getAsJsonObject();

            Class<? extends Message> messageClass;
            try {
                messageClass = (Class<? extends Message>) Class.forName("com.derongan.minecraft.looty.skill.component.proto." + componentName + "Info");
                Method builderMethod = messageClass.getMethod("newBuilder");
                Message.Builder messageBuilder = (Message.Builder) builderMethod.invoke(null);
                JsonFormat.parser().merge(protoInfo.toString(), messageBuilder);

                Class<? extends Component> clazz = (Class<? extends Component>) Class.forName("com.derongan.minecraft.looty.skill.component." + componentName);

                Constructor<? extends Component> componentConstructor = (Constructor<? extends Component>) clazz.getConstructors()[0];

                actionEntityBuilder.addComponent(() -> {
                    try {
                        return componentConstructor.newInstance(messageBuilder.build());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return actionEntityBuilder;
    }
}
