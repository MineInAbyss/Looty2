package com.derongan.generation.looty;

import com.badlogic.ashley.core.Component;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Message;
import com.squareup.javapoet.*;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

// TODO this is very hacky right now and needs work.
public class ComponentGenerationTask extends DefaultTask {
    @TaskAction
    public void run() throws IOException {
        File folder = new File("build/generated/source/proto/main/java/com/derongan/minecraft/looty/skill/component/proto");

        TypeSpec.Builder supplierBuilder = TypeSpec.classBuilder("GeneratedComponentSupplier");
        WildcardTypeName componentWildcard = WildcardTypeName.subtypeOf(ClassName.get(Component.class));
        WildcardTypeName messageWildcard = WildcardTypeName.subtypeOf(ClassName.get(Message.class));
        ParameterizedTypeName componentParamClass = ParameterizedTypeName.get(ClassName.get(Class.class), componentWildcard);
        ParameterizedTypeName messageParamClass = ParameterizedTypeName.get(ClassName.get(Class.class), messageWildcard);
        ParameterizedTypeName componentTypes = ParameterizedTypeName.get(ClassName.get(ImmutableMap.class), messageParamClass, componentParamClass);
        FieldSpec.Builder allComponents = FieldSpec.builder(componentTypes, "componentTypes", Modifier.PRIVATE, Modifier.FINAL);

        Map<ClassName, ClassName> compNameMap = new HashMap();

        for (File file : folder.listFiles()) {
            if (file.getName().endsWith("Info.java")) {
                try {
                    ClassName message = ClassName.get("com.derongan.minecraft.looty.skill.component.proto", file.getName()
                            .split(".java")[0]);
                    ClassName wrapper = generateWrapper(message);
                    compNameMap.put(message, wrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        CodeBlock.Builder setBuilder = CodeBlock.builder();
        setBuilder.add("$T.<$T<? extends $T>, $T<? extends $T>>builder()", ImmutableMap.class, Class.class, Message.class, Class.class, Component.class);
        compNameMap.forEach((wrapperName, componentName) -> setBuilder.add("\t\n.put($T.class, $T.class)", wrapperName, componentName));
        setBuilder.add("\t\n.build()");

        allComponents.initializer(setBuilder.build());

        supplierBuilder.addField(allComponents.build());

//        ClassName componentRegisterClassName = ClassName.get("com.derongan.minecraft.looty.registration", "ComponentRegister");
//        FieldSpec fieldSpec = FieldSpec.builder(componentRegisterClassName, "componentRegister", Modifier.PRIVATE, Modifier.FINAL).build();
//        supplierBuilder.addField(fieldSpec);
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addAnnotation(Inject.class)
//                .addParameter(componentRegisterClassName, "componentRegister")
//                .addStatement("this.componentRegister = componentRegister")
                .build();

        MethodSpec getMethod = MethodSpec.methodBuilder("get")
                .returns(componentTypes)
                .addStatement("return componentTypes")
                .addModifiers(Modifier.PUBLIC)
                .build();

        supplierBuilder.addMethod(constructor);
        supplierBuilder.addMethod(getMethod);
        supplierBuilder.addModifiers(Modifier.PUBLIC);

        JavaFile.builder("com.derongan.minecraft.looty.skill.component.proto", supplierBuilder.build())
                .build()
                .writeTo(Paths.get("build/generated/source/proto/main/java"));
    }


    private ClassName generateWrapper(ClassName protoClassName) throws IOException {
        //TODO make final after fixing Linger/Other config components that expect mutation
        FieldSpec fieldSpec = FieldSpec.builder(protoClassName, "info", Modifier.PRIVATE).build();

        MethodSpec getInfo = MethodSpec.methodBuilder("getInfo")
                .addStatement("return info")
                .addModifiers(Modifier.PUBLIC)
                .returns(protoClassName)
                .build();

        MethodSpec setInfo = MethodSpec.methodBuilder("setInfo")
                .addParameter(protoClassName, "info")
                .addStatement("this.info = info")
                .addModifiers(Modifier.PUBLIC)
                .build();

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(protoClassName, "info", Modifier.FINAL)
                .addStatement("this.info = info")
                .build();

        String wrapperPackageName = "com.derongan.minecraft.looty.skill.component";
        String wrapperClassName = protoClassName.simpleName().split("Info")[0];
        MethodSpec fromProto = MethodSpec.methodBuilder("fromProto")
                .addParameter(protoClassName, "info", Modifier.FINAL)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ClassName.get(wrapperPackageName, wrapperClassName))
                .addStatement("return new $T(info)", ClassName.get(wrapperPackageName, wrapperClassName))
                .build();

        TypeSpec comp = TypeSpec.classBuilder(wrapperClassName)
                .addSuperinterface(TypeName.get(Component.class))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(fieldSpec)
                .addMethod(getInfo)
                .addMethod(setInfo)
                .addMethod(constructor)
                .addMethod(fromProto)
                .build();

        JavaFile javaFile = JavaFile.builder(wrapperPackageName, comp)
                .build();

        javaFile.writeTo(Paths.get("build/generated/source/proto/main/java"));

        return ClassName.get(wrapperPackageName, wrapperClassName);
    }
}
