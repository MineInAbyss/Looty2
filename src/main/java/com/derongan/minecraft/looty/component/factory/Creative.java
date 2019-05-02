package com.derongan.minecraft.looty.component.factory;

import com.derongan.minecraft.looty.ActionEntityBuilder;
import com.derongan.minecraft.looty.component.Component;

public class Creative implements Component {
    private final ActionEntityBuilder actionEntityBuilder;

    private Creative(ActionEntityBuilder actionEntityBuilder) {
        this.actionEntityBuilder = actionEntityBuilder;
    }

    public ActionEntityBuilder getActionEntityBuilder() {
        return actionEntityBuilder;
    }

    public Creative create(ActionEntityBuilder actionEntityBuilder) {
        return new Creative(actionEntityBuilder);
    }
}
