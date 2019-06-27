package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.skill.component.proto.LingerInfo;
import com.derongan.minecraft.looty.skill.proto.Action;
import com.derongan.minecraft.looty.skill.proto.Skill;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

class Scratch {
    public static void main(String[] args) throws ClassNotFoundException, InvalidProtocolBufferException {
        Skill skill = Skill.newBuilder()
                .addAction(Action.newBuilder().addComponents(Any.pack(LingerInfo.newBuilder().setNumberOfTicks(10).build())))
                .build();

        Any any = skill.getActionList().get(0).getComponentsList().get(0);

        String clazzName = any.getTypeUrl().split("/")[1];

        Class<Message> clazz = (Class<Message>)Class.forName(clazzName);

        Message message = any.unpack(clazz);

        System.out.println(message);
    }
}