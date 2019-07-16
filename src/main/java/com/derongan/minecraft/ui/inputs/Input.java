package com.derongan.minecraft.ui.inputs;

import java.util.function.Consumer;

public interface Input<E> {
    E getResult();

    void setSubmitAction(Consumer<E> consumer);

    Consumer<E> getSubmitAction();

    default void onSubmit() {
        getSubmitAction().accept(getResult());
    }
}
