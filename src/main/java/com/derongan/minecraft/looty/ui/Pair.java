package com.derongan.minecraft.looty.ui;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Pair<E> {
    public abstract E getFirst();
    public abstract E getSecond();

    public static <E> Pair<E> create(E newFirst, E newSecond) {
        return new AutoValue_Pair<>(newFirst, newSecond);
    }
}
