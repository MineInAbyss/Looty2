package com.derongan.generation.looty;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ComponentGenerationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        ComponentGenerationTask task = target.getTasks().create("createComponents", ComponentGenerationTask.class);
        task.dependsOn("generateProto");
        target.getTasksByName("compileJava", false).stream().findAny().get().dependsOn("createComponents");
    }
}
