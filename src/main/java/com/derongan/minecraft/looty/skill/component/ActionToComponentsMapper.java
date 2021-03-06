package com.derongan.minecraft.looty.skill.component;

import com.badlogic.ashley.core.Component;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Maps an {@link Action} to a set of unpacked and wrapped {@link Component}s
 */
public class ActionToComponentsMapper {
    private final ComponentRegister componentRegister;

    @Inject
    public ActionToComponentsMapper(ComponentRegister componentRegister) {
        this.componentRegister = componentRegister;
    }

    public Set<Component> convert(Action action) {
        return action.getComponentList().stream().map(this::unpackMessage).map(this::wrap).collect(toImmutableSet());
    }

    private Message unpackMessage(Any any) {
        String clazzName = any.getTypeUrl().split("/")[1];
        try {
            Class<? extends Message> clazz = (Class<? extends Message>) Class.forName(clazzName);
            return any.unpack(clazz);
        } catch (ClassNotFoundException | InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private Component wrap(Message message) {
        Class<? extends Component> componentClass = componentRegister.getComponentForMessage(message.getClass());
        try {
            Method method = componentClass.getMethod("fromProto", message.getClass());
            return (Component) method.invoke(null, message);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
