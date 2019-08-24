package com.derongan.minecraft.looty;

import com.derongan.minecraft.looty.skill.component.proto.Offset;
import com.derongan.minecraft.looty.skill.component.proto.TargetingInfo;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

class Scratch {
    public static void main(String[] args) throws ClassNotFoundException, InvalidProtocolBufferException {
        TargetingInfo targetingInfo = TargetingInfo.newBuilder()
                .putOffset("hi", Offset.getDefaultInstance())
                .putOffset("no", Offset.getDefaultInstance()).build();

        System.out.println(JsonFormat.printer().print(targetingInfo));
    }
}